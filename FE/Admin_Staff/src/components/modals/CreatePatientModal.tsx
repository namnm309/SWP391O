"use client"
import { useState } from "react"
import { ModalWrapper } from "@/components/ui/modal-wrapper"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { useToast } from "@/hooks/use-toast"
import axios from "@/utils/axiosConfig"

interface CreatePatientModalProps {
  onClose: () => void;
}

export function CreatePatientModal({ onClose }: CreatePatientModalProps) {
  const { toast } = useToast()

  const [formData, setFormData] = useState({
    username: "",
    fullname: "",
    email: "",
    phone: "",
    bod: "",
    gender: "",
  })
  const [isSubmitting, setIsSubmitting] = useState(false)

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target
    setFormData((prev) => ({ ...prev, [name]: value }))
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setIsSubmitting(true)
    try {
      await axios.post("/manage/create-customer", formData, {
        headers: { "Content-Type": "application/json" },
      })
      toast({
        title: "Success",
        description: "Patient created successfully",
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
      title="Create Patient"
      description="Fill in the details to create a new patient (customer)"
      isOpen={true}
      onClose={onClose}
    >
      <form onSubmit={handleSubmit} className="space-y-4">
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
        <div className="space-y-2">
          <Label htmlFor="phone">Phone</Label>
          <Input
            id="phone"
            name="phone"
            value={formData.phone}
            onChange={handleChange}
            required
          />
        </div>
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
        <div className="space-y-2">
          <Label htmlFor="gender">Gender</Label>
          <Input
            id="gender"
            name="gender"
            placeholder="Male / Female / Other"
            value={formData.gender}
            onChange={handleChange}
            required
          />
        </div>
        <div className="flex justify-end space-x-2 pt-4">
          <Button type="button" variant="outline" onClick={onClose}>
            Cancel
          </Button>
          <Button type="submit" disabled={isSubmitting}>
            {isSubmitting ? "Creating..." : "Create Patient"}
          </Button>
        </div>
      </form>
    </ModalWrapper>
  )
}
