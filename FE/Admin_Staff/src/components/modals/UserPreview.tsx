import { Patient } from "@/models/user"
import { GenderUser, RoleUser } from "@/types/enums"
import { User, Mail, Phone, Calendar } from "lucide-react"
import { PreviewCard } from "@/components/ui/preview-card"
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar"
import { Badge } from "@/components/ui/badge"
import { Separator } from "../ui/separator"

interface UserPreviewProps {
  user: Patient
  onClose: () => void
}

export function UserPreview({ user, onClose }: UserPreviewProps) {

  const handleEdit = () => {
    // openModal("editUser")
  }

  const formatDate = (dateString?: string) => {
    if (!dateString) return "Not provided"
    return new Date(dateString).toLocaleDateString("en-US", {
      year: "numeric",
      month: "long",
      day: "numeric",
    })
  }

  const getRoleBadgeColor = (role: RoleUser) => {
    switch (role) {
      case RoleUser.ADMIN:
        return "bg-red-100 text-red-800"
      case RoleUser.STAFF:
        return "bg-blue-100 text-blue-800"
      case RoleUser.CUSTOMER:
        return "bg-green-100 text-green-800"
      case RoleUser.CHILD:
        return "bg-purple-100 text-purple-800"
      default:
        return ""
    }
  }

  return (
    <PreviewCard title="User Details" onClose={onClose} onEdit={handleEdit}>
      <div className="space-y-4">
        <div className="flex items-center gap-4">
          <Avatar className="h-16 w-16">
            <AvatarImage src={user?.avatar || ""} alt={user.fullname} />
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
            <Badge className={getRoleBadgeColor(user.role as RoleUser)}>{user.role.replace("ROLE_", "")}</Badge>
          </div>
        </div>

        <Separator />

        <div className="grid grid-cols-1 gap-3">
          <div className="flex items-center gap-2">
            <Mail className="h-4 w-4 text-gray-500" />
            <span>{user.email}</span>
          </div>

          <div className="flex items-center gap-2">
            <Phone className="h-4 w-4 text-gray-500" />
            <span>{user.phone}</span>
          </div>

          <div className="flex items-center gap-2">
            <User className="h-4 w-4 text-gray-500" />
            <span>{user.gender === GenderUser.MALE ? "Male" : "Female"}</span>
          </div>

          {user.dob && (
            <div className="flex items-center gap-2">
              <Calendar className="h-4 w-4 text-gray-500" />
              <span>Born: {formatDate(user.dob)}</span>
            </div>
          )}
        </div>

        {user.address && (
          <>
            <Separator />
            <div className="space-y-1">
              <h3 className="text-sm font-medium text-gray-500">Address</h3>
              <p className="text-sm">{user.address}</p>
            </div>
          </>
        )}
      </div>
    </PreviewCard>
  )
}

