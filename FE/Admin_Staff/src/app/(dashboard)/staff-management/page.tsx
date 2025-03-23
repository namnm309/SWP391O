"use client"
import { useState, useEffect } from "react"
import type { ColumnDef } from "@tanstack/react-table"
import { Edit, Trash2, Eye, UserPlus, Lock, Unlock, CheckCircle, XCircle } from "lucide-react"
import { useStore } from "@/store"
import { DataTable } from "@/components/ui/data-table"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { useToast } from "@/hooks/use-toast"
import { Badge } from "@/components/ui/badge"
import type { Role, StaffMember } from "@/types/staff"
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
import { StaffDetailsModal } from "@/components/modals/StaffDetail"
import axios from "@/utils/axiosConfig"
import { CreateStaffModal } from "@/components/modals/CreateStaffModal"

export default function StaffManagementPage() {
  const { toast } = useToast()

  const [staffMembers, setStaffMembers] = useState<StaffMember[]>([])
  const [loading, setLoading] = useState(true)
  const [selectedStaff, setSelectedStaff] = useState<StaffMember | null>(null)
  const [isCreateStaffModalOpen, setIsCreateStaffModalOpen] = useState(false)
  const [isDetailsModalOpen, setIsDetailsModalOpen] = useState(false)
  const [deleteDialogOpen, setDeleteDialogOpen] = useState(false)
  const [staffToDelete, setStaffToDelete] = useState<number | null>(null)

  useEffect(() => {
    const loadStaffMembers = async () => {
      try {
        const response = await axios.get("/manage/staff-list")
        const data: StaffMember[] = response.data.result || []
        setStaffMembers(data)
      } catch (error) {
        toast({
          title: "Error",
          description: "Failed to load staff members",
          variant: "destructive",
        })
      } finally {
        setLoading(false)
      }
    }

    loadStaffMembers()
  }, [toast])

  const handleViewStaffDetails = (id: number) => {
    const staff = staffMembers.find(s => s.id === id)
    if (staff) {
      setSelectedStaff(staff)
      setIsDetailsModalOpen(true)
    }
  }

  const handleEditStaff = (id: number) => {
    const staff = staffMembers.find(s => s.id === id)
    if (staff) {
      // Implement edit logic here
    }
  }

  const confirmDelete = (id: number) => {
    setStaffToDelete(id)
    setDeleteDialogOpen(true)
  }

  const handleDelete = async () => {
    if (!staffToDelete) return

    try {
      // Call your API to delete the staff member here if needed
      // For now, we update the local state
      setStaffMembers(staffMembers.filter(staff => staff.id !== staffToDelete))
      toast({
        title: "Success",
        description: "Staff member deleted successfully",
      })
    } catch (error) {
      toast({
        title: "Error",
        description: "Failed to delete staff member",
        variant: "destructive",
      })
    } finally {
      setStaffToDelete(null)
      setDeleteDialogOpen(false)
    }
  }

  const toggleStaffStatus = (id: number) => {
    setStaffMembers(staffMembers.map(staff => 
      staff.id === id ? { ...staff, enabled: !staff.enabled } : staff
    ))
    toast({
      title: "Success",
      description: "Staff status updated successfully",
    })
  }

  const toggleAccountLock = (id: number) => {
    setStaffMembers(staffMembers.map(staff => 
      staff.id === id ? { ...staff, accountNonLocked: !staff.accountNonLocked } : staff
    ))
    toast({
      title: "Success",
      description: `Account ${staffMembers.find(s => s.id === id)?.accountNonLocked ? 'locked' : 'unlocked'} successfully`,
    })
  }

  const columns: ColumnDef<StaffMember>[] = [
    {
      accessorKey: "id",
      header: "ID",
    },
    {
      accessorKey: "fullname",
      header: "Name",
    },
    {
      accessorKey: "email",
      header: "Email",
    },
    {
      accessorKey: "roles",
      header: "Role",
      cell: ({ row }) => {
        const roles = row.getValue("roles") as Role[]
        return (
          <div className="flex flex-wrap gap-1">
            {roles.map((role, index) => (
              <Badge key={index} variant="outline">
                {role.name.replace('ROLE_', '')}
              </Badge>
            ))}
          </div>
        )
      },
    },
    {
      accessorKey: "enabled",
      header: "Status",
      cell: ({ row }) => {
        const enabled = row.getValue("enabled") as boolean
        return enabled ? 
          <Badge className="bg-green-100 text-green-800">Active</Badge> : 
          <Badge className="bg-red-100 text-red-800">Inactive</Badge>
      },
    },
    {
      accessorKey: "accountNonLocked",
      header: "Account",
      cell: ({ row }) => {
        const accountNonLocked = row.getValue("accountNonLocked") as boolean
        return accountNonLocked ? 
          <Badge className="bg-blue-100 text-blue-800">Unlocked</Badge> : 
          <Badge className="bg-yellow-100 text-yellow-800">Locked</Badge>
      },
    },
    {
      id: "actions",
      header: "Actions",
      cell: ({ row }) => {
        const id = row.getValue("id") as number
        const enabled = row.getValue("enabled") as boolean
        const accountNonLocked = row.getValue("accountNonLocked") as boolean

        return (
          <div className="flex gap-2">
            <Button variant="outline" size="sm" onClick={() => handleViewStaffDetails(id)}>
              <Eye className="h-4 w-4" />
            </Button>
            {/* <Button variant="outline" size="sm" onClick={() => handleEditStaff(id)}>
              <Edit className="h-4 w-4" />
            </Button> */}
            {/* <Button 
              variant="outline" 
              size="sm" 
              onClick={() => toggleStaffStatus(id)}
              className={enabled ? "text-red-500 hover:text-red-700" : "text-green-500 hover:text-green-700"}
            >
              {enabled ? <XCircle className="h-4 w-4" /> : <CheckCircle className="h-4 w-4" />}
            </Button> */}
            {/* <Button 
              variant="outline" 
              size="sm" 
              onClick={() => toggleAccountLock(id)}
              className={accountNonLocked ? "text-yellow-500 hover:text-yellow-700" : "text-blue-500 hover:text-blue-700"}
            >
              {accountNonLocked ? <Lock className="h-4 w-4" /> : <Unlock className="h-4 w-4" />}
            </Button> */}
            {/* <Button
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
        <h1 className="text-2xl font-bold">Staff Management</h1>
        <Button onClick={() => setIsCreateStaffModalOpen(true)}>
          <UserPlus className="mr-2 h-4 w-4" />
          Add Staff
        </Button>
      </div>

      {/* <div className="grid gap-4 md:grid-cols-3">
        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium">Total Staff</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{staffMembers.length}</div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium">Active Staff</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{staffMembers.filter(s => s.enabled).length}</div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium">Locked Accounts</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{staffMembers.filter(s => !s.accountNonLocked).length}</div>
          </CardContent>
        </Card>
      </div> */}

      <Card>
        <CardHeader>
          <CardTitle>All Staff Members</CardTitle>
        </CardHeader>
        <CardContent>
          {loading ? (
            <div className="flex h-40 items-center justify-center">
              <p>Loading staff members...</p>
            </div>
          ) : (
            <DataTable
              columns={columns}
              data={staffMembers}
              searchColumn="fullname"
              searchPlaceholder="Search by name..."
            />
          )}
        </CardContent>
      </Card>

      {isCreateStaffModalOpen && (
        <CreateStaffModal onClose={() => setIsCreateStaffModalOpen(false)} />
      )}

      <StaffDetailsModal 
        isOpen={isDetailsModalOpen} 
        onClose={() => setIsDetailsModalOpen(false)} 
        staff={selectedStaff} 
      />

      <AlertDialog open={deleteDialogOpen} onOpenChange={setDeleteDialogOpen}>
        <AlertDialogContent>
          <AlertDialogHeader>
            <AlertDialogTitle>Are you sure?</AlertDialogTitle>
            <AlertDialogDescription>
              This action cannot be undone. This will permanently delete the staff member and remove their data from our servers.
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
