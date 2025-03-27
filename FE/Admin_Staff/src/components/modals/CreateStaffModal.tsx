"use client"
import { useState } from "react"
import { ModalWrapper } from "@/components/ui/modal-wrapper"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { useToast } from "@/hooks/use-toast"
import axios from "@/utils/axiosConfig"

export function CreateStaffModal({ onClose }: { onClose: () => void }) {
  const { toast } = useToast()

  const [formData, setFormData] = useState({
    username: "",
    fullname: "",
    password: "",
    email: "",
    phone: "",
    bod: "",
    gender: "Male",
  })

  const [avatarFile, setAvatarFile] = useState<File | null>(null)
  const [isSubmitting, setIsSubmitting] = useState(false)

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target
    setFormData((prev) => ({ ...prev, [name]: value }))
  }

  const handleAvatarChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files.length > 0) {
      setAvatarFile(e.target.files[0])
    }
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setIsSubmitting(true)

    try {
      const fd = new FormData()
      fd.append("username", formData.username)
      fd.append("fullname", formData.fullname)
      fd.append("password", formData.password)
      fd.append("email", formData.email)
      fd.append("phone", formData.phone)
      fd.append("bod", formData.bod)
      fd.append("gender", formData.gender)
      if (avatarFile) {
        fd.append("avatar", avatarFile)
      }

      await axios.post("/manage/createStaff", fd, {
        headers: { "Content-Type": "multipart/form-data" },
      })

      toast({
        title: "Success",
        description: "Staff created successfully",
      })
      onClose()
    } catch (error: any) {
      toast({
        title: "Error",
        description: error?.response?.data?.message || error.message,
        variant: "destructive",
      })
    } finally {
      setIsSubmitting(false)
    }
  }

  return (
    <ModalWrapper
      title="Create Staff"
      description="Fill in the details to create a new staff member"
      isOpen={true}
      onClose={onClose}
    >
      <form onSubmit={handleSubmit} className="space-y-4">
        {/* Username */}
        <div className="space-y-2">
          <Label htmlFor="username">Username</Label>
          <Input
            id="username"
            name="username"
            value={formData.username}
            onChange={handleChange}
            required
          />
        </div>
        {/* Full Name */}
        <div className="space-y-2">
          <Label htmlFor="fullname">Full Name</Label>
          <Input
            id="fullname"
            name="fullname"
            value={formData.fullname}
            onChange={handleChange}
            required
          />
        </div>
        {/* Password */}
        <div className="space-y-2">
          <Label htmlFor="password">Password</Label>
          <Input
            id="password"
            name="password"
            type="password"
            value={formData.password}
            onChange={handleChange}
            required
          />
        </div>
        {/* Email */}
        <div className="space-y-2">
          <Label htmlFor="email">Email</Label>
          <Input
            id="email"
            name="email"
            type="email"
            value={formData.email}
            onChange={handleChange}
            required
          />
        </div>
        {/* Phone */}
        <div className="space-y-2">
          <Label htmlFor="phone">Phone</Label>
          <Input
            id="phone"
            name="phone"
            type="tel"
            value={formData.phone}
            onChange={handleChange}
            required
          />
        </div>
        {/* Date of Birth */}
        <div className="space-y-2">
          <Label htmlFor="bod">Date of Birth</Label>
          <Input
            id="bod"
            name="bod"
            type="date"
            value={formData.bod}
            onChange={handleChange}
            required
          />
        </div>
        {/* Gender */}
        <div className="space-y-2">
          <Label htmlFor="gender">Gender</Label>
          <select
            id="gender"
            name="gender"
            value={formData.gender}
            onChange={(e) => setFormData((prev) => ({ ...prev, gender: e.target.value }))}
            className="w-full rounded-md border border-gray-300 p-2"
          >
            <option value="Male">Male</option>
            <option value="Female">Female</option>
            <option value="Other">Other</option>
          </select>
        </div>
        {/* Avatar */}
        <div className="space-y-2">
          <Label htmlFor="avatar">Avatar</Label>
          <Input
            id="avatar"
            name="avatar"
            type="file"
            accept="image/*"
            onChange={handleAvatarChange}
          />
        </div>
        {/* Form Buttons */}
        <div className="flex justify-end space-x-2 pt-4">
          <Button type="button" variant="outline" onClick={onClose}>
            Cancel
          </Button>
          <Button type="submit" disabled={isSubmitting}>
            {isSubmitting ? "Creating..." : "Create Staff"}
          </Button>
        </div>
      </form>
    </ModalWrapper>
  )
}
