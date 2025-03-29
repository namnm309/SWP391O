"use client"
import { useState } from "react"
import { ModalWrapper } from "@/components/ui/modal-wrapper"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { useToast } from "@/hooks/use-toast"
import axios from "@/utils/axiosConfig"

export function CreateChildModal({ onClose }: { onClose: () => void }) {
  const { toast } = useToast()

  const [formData, setFormData] = useState({
    parentId: "",
    fullname: "",
    dob: "",
    gender: "",
    height: "",
    weight: "",
    relationshipType: "CHA_ME",
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
      if (!formData.parentId) {
        toast({
          title: "Error",
          description: "Please enter Parent ID",
          variant: "destructive",
        })
        return
      }
      const fd = new FormData()
      fd.append("fullname", formData.fullname)
      fd.append("dob", formData.dob)
      fd.append("gender", formData.gender)
      fd.append("height", formData.height)
      fd.append("weight", formData.weight)
      fd.append("relationshipType", formData.relationshipType)
      if (avatarFile) {
        fd.append("avatar", avatarFile)
      }
      await axios.post(`/manage/children/create/${Number(formData.parentId)}`, fd, {
        headers: { "Content-Type": "multipart/form-data" },
      })
      toast({
        title: "Success",
        description: "Child created successfully",
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
      title="Create Child"
      description="Fill in the details to create a new child record"
      isOpen={true}
      onClose={onClose}
    >
      <form onSubmit={handleSubmit} className="space-y-4">
        {/* Parent ID */}
        <div className="space-y-2">
          <Label htmlFor="parentId">Parent ID</Label>
          <Input
            id="parentId"
            name="parentId"
            type="number"
            value={formData.parentId}
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
        {/* Date of Birth */}
        <div className="space-y-2">
          <Label htmlFor="dob">Date of Birth</Label>
          <Input
            id="dob"
            name="dob"
            type="date"
            value={formData.dob}
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
            onChange={(e) =>
              setFormData((prev) => ({ ...prev, gender: e.target.value }))
            }
            className="w-full rounded-md border border-gray-300 p-2"
            required
          >
            <option value="">Select gender</option>
            <option value="Male">Male</option>
            <option value="Female">Female</option>
            <option value="Other">Other</option>
          </select>
        </div>
        {/* Height */}
        <div className="space-y-2">
          <Label htmlFor="height">Height (cm)</Label>
          <Input
            id="height"
            name="height"
            type="number"
            value={formData.height}
            onChange={handleChange}
            required
          />
        </div>
        {/* Weight */}
        <div className="space-y-2">
          <Label htmlFor="weight">Weight (kg)</Label>
          <Input
            id="weight"
            name="weight"
            type="number"
            value={formData.weight}
            onChange={handleChange}
            required
          />
        </div>
        {/* Relationship Type */}
        <div className="space-y-2">
          <Label htmlFor="relationshipType">Relationship Type</Label>
          <select
            id="relationshipType"
            name="relationshipType"
            value={formData.relationshipType}
            onChange={(e) =>
              setFormData((prev) => ({ ...prev, relationshipType: e.target.value }))
            }
            className="w-full rounded-md border border-gray-300 p-2"
            required
          >
            <option value="CHA_ME">Cha/Mẹ</option>
            <option value="ONG_BA">Ông/Bà</option>
            <option value="ANH_CHI">Anh/Chị</option>
            <option value="CHU_THIEM">Chú/Thím</option>
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
        <div className="flex justify-end space-x-2 pt-4">
          <Button type="button" variant="outline" onClick={onClose}>
            Cancel
          </Button>
          <Button type="submit" disabled={isSubmitting}>
            {isSubmitting ? "Creating..." : "Create Child"}
          </Button>
        </div>
      </form>
    </ModalWrapper>
  )
}