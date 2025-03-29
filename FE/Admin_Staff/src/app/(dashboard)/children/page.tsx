"use client"
import { useState, useEffect, useCallback } from "react"
import type { ColumnDef } from "@tanstack/react-table"
import { Eye, UserPlus } from "lucide-react"
import { DataTable } from "@/components/ui/data-table"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { useToast } from "@/hooks/use-toast"
import type { Child } from "@/types/user"
import axios from "@/utils/axiosConfig"
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
import { ChildDetailsModal } from "@/components/modals/ChildDetail"
import { ApiError } from "@/types/error"
import { CreateChildModal } from "@/components/modals/CreateChildModal"

export default function UsersManagementPage() {
  const { toast } = useToast()

  const [users, setUsers] = useState<Child[]>([])
  const [loading, setLoading] = useState(true)
  const [selectedUser, setSelectedUser] = useState<Child | null>(null)
  const [isDetailsModalOpen, setIsDetailsModalOpen] = useState(false)
  const [deleteDialogOpen, setDeleteDialogOpen] = useState(false)
  const [userToDelete, setUserToDelete] = useState<number | null>(null)

  const [isChildModalOpen, setIsChildModalOpen] = useState(false)

  const loadUsers = useCallback(async () => {
    try {
      setLoading(true)
      const token = localStorage.getItem("token")
      const resp = await axios.get("/manage/children", {
        headers: { Authorization: `Bearer ${token}` },
      })
      const data: Child[] = resp.data.result || resp.data || []
      setUsers(data)
    } catch (error: unknown) {
      const msg = error instanceof Error ? error.message : "Failed to load users";
      toast({
        title: "Error",
        description: msg,
        variant: "destructive",
      })
    } finally {
      setLoading(false)
    }
  }, [toast])

  useEffect(() => {
    loadUsers()
  }, [loadUsers])

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString("en-US", {
      year: "numeric",
      month: "long",
      day: "numeric",
    })
  }

  const handleViewUserDetails = (id: number) => {
    const user = users.find((u) => u.childId === id)
    if (user) {
      setSelectedUser(user)
      setIsDetailsModalOpen(true)
    }
  }

  const handleDelete = async () => {
    if (!userToDelete) return
    try {
      setUsers((prev) => prev.filter((user) => user.childId !== userToDelete))
      toast({
        title: "Success",
        description: "User deleted successfully",
      })
    } catch (error: unknown) {
      const msg = error instanceof Error ? error.message : "Failed to delete user";
      toast({
        title: "Error",
        description: msg,
        variant: "destructive",
      })
    } finally {
      setUserToDelete(null)
      setDeleteDialogOpen(false)
    }
  }

  const columns: ColumnDef<Child>[] = [
    {
      accessorKey: "childId",
      header: "ID",
      cell: ({row}) => row.getValue("childId") || "-"
    },
    {
      accessorKey: "fullname",
      header: "Name",
    },
    
    {
      accessorKey: "birthDate",
      header: "DoB",
      cell: ({row}) => {
        return row.getValue("birthDate") ? formatDate(row.getValue("birthDate")) : "-" 
      }
    },
    {
      accessorKey: "gender",
      header: "Gender",
      cell: ({row}) => {
        return <span className="capitalize">{row.getValue("gender") || "-"}</span>
      }
    },
    {
      id: "actions",
      header: "Actions",
      cell: ({ row }) => {
        const id = row.getValue("childId") as number

        return (
          <div className="flex gap-2">
            <Button variant="outline" size="sm" onClick={() => handleViewUserDetails(id)}>
              <Eye className="h-4 w-4" />
            </Button>
            {/* Uncomment these if you add edit/delete logic:
            <Button variant="outline" size="sm" onClick={() => handleEditUser(id)}>
              <Edit className="h-4 w-4" />
            </Button>
            <Button
              variant="outline"
              size="sm"
              onClick={() => confirmDelete(id)}
              className="text-red-500 hover:text-red-700"
            >
              <Trash2 className="h-4 w-4" />
            </Button> */}
          </div>
        )
      },
    },
  ]

  return (
    <div className="space-y-4">
      <div className="flex items-center justify-between">
        <h1 className="text-2xl font-bold">Children Management</h1>
        <Button onClick={() => setIsChildModalOpen(true)}>
          <UserPlus className="mr-2 h-4 w-4" />
          Add Child
        </Button>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>All Users</CardTitle>
        </CardHeader>
        <CardContent>
          {loading ? (
            <div className="flex h-40 items-center justify-center">
              <p>Loading users...</p>
            </div>
          ) : (
            <DataTable
              columns={columns}
              data={users}
              searchColumn="fullname"
              searchPlaceholder="Search by name..."
            />
          )}
        </CardContent>
      </Card>

      {/* User Details Modal */}
      <ChildDetailsModal
        isOpen={isDetailsModalOpen}
        onClose={() => setIsDetailsModalOpen(false)}
        user={selectedUser}
      />

      {isChildModalOpen && (
        <CreateChildModal onClose={() => setIsChildModalOpen(false)} />
      )}

      <AlertDialog open={deleteDialogOpen} onOpenChange={setDeleteDialogOpen}>
        <AlertDialogContent>
          <AlertDialogHeader>
            <AlertDialogTitle>Are you sure?</AlertDialogTitle>
            <AlertDialogDescription>
              This action cannot be undone. This will permanently delete the user and remove their data from our
              servers.
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
