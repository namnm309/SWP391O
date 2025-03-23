"use client"
import { useState, useEffect } from "react"
import type { ColumnDef } from "@tanstack/react-table"
import { Eye } from "lucide-react"
import { DataTable } from "@/components/ui/data-table"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { useToast } from "@/hooks/use-toast"
import type { User } from "@/types/user"
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
import { UserDetailsModal } from "@/components/modals/UserDetail"

export default function UsersManagementPage() {
  const { toast } = useToast()

  const [users, setUsers] = useState<User[]>([])
  const [loading, setLoading] = useState(true)
  const [selectedUser, setSelectedUser] = useState<User | null>(null)
  const [isDetailsModalOpen, setIsDetailsModalOpen] = useState(false)
  const [deleteDialogOpen, setDeleteDialogOpen] = useState(false)
  const [userToDelete, setUserToDelete] = useState<number | null>(null)

  const loadUsers = async () => {
    try {
      setLoading(true)
      const token = localStorage.getItem("token")
      const resp = await axios.get("/manage/parents", {
        headers: { Authorization: `Bearer ${token}` },
      })
      const data: User[] = resp.data.result || resp.data || []
      setUsers(data)
    } catch (error) {
      toast({
        title: "Error",
        description: "Failed to load users",
        variant: "destructive",
      })
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    loadUsers()
  }, [toast])

  const handleViewUserDetails = (id: number) => {
    const user = users.find((u) => u.id === id)
    if (user) {
      setSelectedUser(user)
      setIsDetailsModalOpen(true)
    }
  }

  const confirmDelete = (id: number) => {
    setUserToDelete(id)
    setDeleteDialogOpen(true)
  }

  const handleDelete = async () => {
    if (!userToDelete) return
    try {
      // If there's an API endpoint to delete the user, call it here.
      // For now, just remove from local state:
      setUsers((prev) => prev.filter((user) => user.id !== userToDelete))
      toast({
        title: "Success",
        description: "User deleted successfully",
      })
    } catch (error) {
      toast({
        title: "Error",
        description: "Failed to delete user",
        variant: "destructive",
      })
    } finally {
      setUserToDelete(null)
      setDeleteDialogOpen(false)
    }
  }

  const columns: ColumnDef<User>[] = [
    {
      accessorKey: "id",
      header: "ID",
    },
    {
      accessorKey: "fullname",
      header: "Name",
    },
    {
      accessorKey: "username",
      header: "Username",
    },
    {
      accessorKey: "email",
      header: "Email",
    },
    {
      accessorKey: "phone",
      header: "Phone",
    },
    {
      accessorKey: "gender",
      header: "Gender",
    },
    {
      id: "actions",
      header: "Actions",
      cell: ({ row }) => {
        const id = row.getValue("id") as number

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
        <h1 className="text-2xl font-bold">User Management</h1>
        {/* If you want an "Add User" button, uncomment:
        <Button onClick={() => {}}>
          <UserPlus className="mr-2 h-4 w-4" />
          Add User
        </Button> */}
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
      <UserDetailsModal
        isOpen={isDetailsModalOpen}
        onClose={() => setIsDetailsModalOpen(false)}
        user={selectedUser}
      />

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
