"use client"
import { useState, useEffect, useMemo, useCallback } from "react"
import type { ColumnDef } from "@tanstack/react-table"
import { Edit, Trash2, Plus, Eye, Search } from "lucide-react"
import type { Vaccine } from "@/types/vaccine"
import { useStore } from "@/store"
import { DataTable } from "@/components/ui/data-table"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { useToast } from "@/hooks/use-toast"
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
} from "@/components/ui/alert-dialog"
import { VaccinePreview } from "@/components/modals/VaccinePreview"
import { VaccineModal } from "@/components/modals/VaccineModal"

export default function VaccinesPage() {
  const { fetchVaccines, deleteVaccine } = useStore.getState()
  const { toast } = useToast()

  const [vaccines, setVaccines] = useState<Vaccine[]>([])
  const [loading, setLoading] = useState(true)
  const [deleteDialogOpen, setDeleteDialogOpen] = useState(false)
  const [isCreateModalOpen, setIsCreateModalOpen] = useState(false)
  const [isUpdateModalOpen, setIsUpdateModalOpen] = useState<Vaccine | undefined>(undefined)
  const [vaccineToDelete, setVaccineToDelete] = useState<number | null>(null)
  const [selectedVaccine, setSelectedVaccine] = useState<Vaccine | null>(null)

  const [searchText, setSearchText] = useState("")
  const [selectedCategoryId, setSelectedCategoryId] = useState<string>("all")

  const loadVaccines = useCallback(async () => {
    try {
      setLoading(true)
      const data = await fetchVaccines()
      setVaccines(data)
    } catch (error) {
      toast({
        title: "Error",
        description: "Failed to load vaccines",
        variant: "destructive",
      })
    } finally {
      setLoading(false)
    }
  }, [fetchVaccines, toast])

  useEffect(() => {
    loadVaccines()
  }, [loadVaccines])

  const handlePreview = (vaccineId: number) => {
    const vaccine = vaccines.find((v) => v.id === vaccineId)
    if (vaccine) {
      setSelectedVaccine(vaccine)
    }
  }

  const handleEdit = (vaccineId: number) => {
    const vaccine = vaccines.find((v) => v.id === vaccineId)
    if (vaccine) {
      setIsUpdateModalOpen(vaccine)
    }
  }

  const handleDelete = async () => {
    if (!vaccineToDelete) return

    try {
      await deleteVaccine(vaccineToDelete)
      setVaccines(vaccines.filter((vaccine) => vaccine.id !== vaccineToDelete))
      toast({
        title: "Success",
        description: "Vaccine deleted successfully",
      })
    } catch (error) {
      toast({
        title: "Error",
        description: "Failed to delete vaccine",
        variant: "destructive",
      })
    } finally {
      setVaccineToDelete(null)
      setDeleteDialogOpen(false)
    }
  }

  const confirmDelete = (vaccineId: number) => {
    setVaccineToDelete(vaccineId)
    setDeleteDialogOpen(true)
  }

  const columns: ColumnDef<Vaccine>[] = [
    {
      accessorKey: "id",
      header: "ID",
    },
    {
      accessorKey: "title",
      header: "Vaccine Name",
    },
    {
      accessorKey: "category.name",
      header: "Category Name",
    },
    {
      accessorKey: "price",
      header: "Price",
      cell: ({ row }) => {
        const price = row.getValue("price") as number
        return new Intl.NumberFormat("vn-VN", {
          style: "currency",
          currency: "vnd",
        }).format(price)
      },
    },
    {
      accessorKey: "stock",
      header: "Stock",
      cell: ({ row }) => {
        const stock = row.getValue("stock") as number
        return stock
      },
    },
    {
      id: "actions",
      header: "Actions",
      cell: ({ row }) => {
        const vaccineId = row.getValue("id") as number

        return (
          <div className="flex gap-2">
            <Button variant="outline" size="sm" onClick={() => handlePreview(vaccineId)}>
              <Eye className="h-4 w-4" />
            </Button>
            <Button variant="outline" size="sm" onClick={() => handleEdit(vaccineId)}>
              <Edit className="h-4 w-4" />
            </Button>
            <Button
              variant="outline"
              size="sm"
              onClick={() => confirmDelete(vaccineId)}
              className="text-red-500 hover:text-red-700"
            >
              <Trash2 className="h-4 w-4" />
            </Button>
          </div>
        )
      },
    },
  ]

  const categories = useMemo(() => {
    const catsMap = new Map<number, { id: number; name: string }>()
    vaccines.forEach((v) => {
      if (v.category) {
        catsMap.set(v.category.id, { id: v.category.id, name: v.category.name.trim() })
      }
    })
    return Array.from(catsMap.values())
  }, [vaccines])

  const filteredVaccines = useMemo(() => {
    return vaccines.filter((v) => {
      const search = searchText.toLowerCase();
      const matchesSearch =
        v.title.toLowerCase().includes(search) ||
        v.id.toString().includes(searchText);
      const matchesCategory =
        selectedCategoryId === "all" ||
        (v.category && v.category.id === Number(selectedCategoryId));
      return matchesSearch && matchesCategory;
    });
  }, [vaccines, searchText, selectedCategoryId]);
  

  return (
    <div className="space-y-4">
      <div className="flex items-center justify-between">
        <h1 className="text-2xl font-bold">Vaccines Management</h1>
        <Button onClick={() => setIsCreateModalOpen(true)}>
          <Plus className="mr-2 h-4 w-4" />
          Add Vaccine
        </Button>
      </div>

      <div className="flex items-center space-x-2">
        <div className="relative flex-1">
          <input
            type="text"
            placeholder="Search by vaccine id, name..."
            value={searchText}
            onChange={(e) => setSearchText(e.target.value)}
            className="w-full rounded-md border border-gray-300 p-2 pl-10"
          />
          <span className="absolute left-3 top-2.5 text-gray-500">
            <Search />
          </span>
        </div>
        <select
          value={selectedCategoryId}
          onChange={(e) => setSelectedCategoryId(e.target.value)}
          className="rounded-md border border-gray-300 p-2"
        >
          <option value="all">All Categories</option>
          {categories.map((cat) => (
            <option key={cat.id} value={cat.id}>
              {cat.name}
            </option>
          ))}
        </select>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>All Vaccines</CardTitle>
        </CardHeader>
        <CardContent>
          {loading ? (
            <div className="flex h-40 items-center justify-center">
              <p>Loading vaccines...</p>
            </div>
          ) : (
            <DataTable
              columns={columns}
              data={filteredVaccines}
              searchPlaceholder="Search by id or title..."
            />
          )}
        </CardContent>
      </Card>

      {/* Create Popup */}
      {isCreateModalOpen && (
        <VaccineModal onClose={() => setIsCreateModalOpen(false)} />
      )}

      {/* Preview Popup */}
      {selectedVaccine && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50 p-4">
          <VaccinePreview vaccine={selectedVaccine} onClose={() => setSelectedVaccine(null)} />
        </div>
      )}

      {/* Update Popup */}
      {isUpdateModalOpen && (
        <VaccineModal onClose={() => setIsUpdateModalOpen(undefined)} vaccine={isUpdateModalOpen} />
      )}

      <AlertDialog open={deleteDialogOpen} onOpenChange={setDeleteDialogOpen}>
        <AlertDialogContent>
          <AlertDialogHeader>
            <AlertDialogTitle>Are you sure?</AlertDialogTitle>
            <AlertDialogDescription>
              This action cannot be undone. This will permanently delete the vaccine and may affect appointments that
              use this vaccine.
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogCancel>Cancel</AlertDialogCancel>
            <AlertDialogAction onClick={handleDelete} className="bg-red-500 hover:bg-red-600">
              Delete
            </AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>
    </div>
  )
}
