"use client"
import { Mail, Phone, Calendar, User } from "lucide-react"
import { Dialog, DialogContent, DialogHeader, DialogTitle } from "@/components/ui/dialog"
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar"
import { Separator } from "@/components/ui/separator"
import type { User as UserType } from "@/types/user"

interface UserDetailsModalProps {
  isOpen: boolean
  onClose: () => void
  user: UserType | null
}

export function UserDetailsModal({ isOpen, onClose, user }: UserDetailsModalProps) {

  if (!user) return null

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString("en-US", {
      year: "numeric",
      month: "long",
      day: "numeric",
    })
  }

  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent className="max-w-md">
        <DialogHeader>
          <DialogTitle>User Details</DialogTitle>
        </DialogHeader>

        <div className="space-y-6">
          {/* User Profile Header */}
          <div className="flex items-center gap-4">
            <Avatar className="h-16 w-16">
              <AvatarImage src={user.avatarUrl || ""} alt={user.fullname} />
              <AvatarFallback>
                {user.fullname
                  .split(" ")
                  .map((n) => n[0])
                  .join("")
                  .toUpperCase()}
              </AvatarFallback>
            </Avatar>
            <div>
              <h2 className="text-xl font-semibold">{user.fullname}</h2>
              <p className="text-sm text-gray-500">@{user.username}</p>
            </div>
          </div>

          <Separator />

          <div className="space-y-4">
            <div className="space-y-1">
              <div className="flex items-center gap-2 text-sm text-gray-500">
                <Mail className="h-4 w-4" />
                <span>Email</span>
              </div>
              <p className="font-medium">{user.email}</p>
            </div>

            <div className="space-y-1">
              <div className="flex items-center gap-2 text-sm text-gray-500">
                <Phone className="h-4 w-4" />
                <span>Phone</span>
              </div>
              <p className="font-medium">{user.phone}</p>
            </div>

            <div className="space-y-1">
              <div className="flex items-center gap-2 text-sm text-gray-500">
                <Calendar className="h-4 w-4" />
                <span>Date of Birth</span>
              </div>
              <p className="font-medium">{formatDate(user.bod)}</p>
            </div>

            <div className="space-y-1">
              <div className="flex items-center gap-2 text-sm text-gray-500">
                <User className="h-4 w-4" />
                <span>Gender</span>
              </div>
              <p className="font-medium">{user.gender}</p>
            </div>

            {/* <div className="space-y-1">
              <div className="text-sm text-gray-500">Parent Account</div>
              <p className="font-medium">{getParentInfo()}</p>
            </div> */}
          </div>

          {/* <div className="flex justify-end">
            <Button onClick={handleEdit}>Edit User</Button>
          </div> */}
        </div>
      </DialogContent>
    </Dialog>
  )
}

