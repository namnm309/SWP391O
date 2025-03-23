"use client"
import { useState } from "react"
import { useStore } from "@/store"
import { ModalWrapper } from "@/components/ui/modal-wrapper"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Switch } from "@/components/ui/switch"
import { useToast } from "@/hooks/use-toast"
import type { Category } from "@/types/category"

interface CategoryModalProps {
  onClose: () => void;
  category?: Category;
}

export function CategoryModal({ onClose, category }: CategoryModalProps) {
  const { createCategory, updateCategory } = useStore();
  const { toast } = useToast();
  const isUpdateMode = Boolean(category);

  const [formData, setFormData] = useState({
    name: category?.name || "",
    isActive: category?.isActive ?? true,
  });
  const [imageFile, setImageFile] = useState<File | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSwitchChange = (checked: boolean) => {
    setFormData((prev) => ({ ...prev, isActive: checked }));
  };

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if(e.target.files && e.target.files.length > 0) {
      setImageFile(e.target.files[0]);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsSubmitting(true);
    try {
      const fd = new FormData();
      fd.append("name", formData.name);
      if (isUpdateMode && category) {
        fd.append("isActive", JSON.stringify(formData.isActive));
      } else {
        fd.append("active", JSON.stringify(formData.isActive));
      }
      if (imageFile) {
        fd.append("file", imageFile);
      }
      if (isUpdateMode && category) {
        await updateCategory(category.id, fd);
        toast({
          title: "Success",
          description: "Category updated successfully",
        });
      } else {
        await createCategory(fd);
        toast({
          title: "Success",
          description: "Category created successfully",
        });
      }
      onClose();
    } catch (error) {
      toast({
        title: "Error",
        description: "Failed to submit category",
        variant: "destructive",
      });
    } finally {
      setIsSubmitting(false);
    }
  };
    

  return (
    <ModalWrapper
      title={isUpdateMode ? "Update Category" : "Create Category"}
      description={isUpdateMode ? "Update category information" : "Fill in details to create a category"}
      isOpen={true}
      onClose={onClose}
    >
      <form onSubmit={handleSubmit} className="space-y-4">
        <div className="space-y-2">
          <Label htmlFor="name">Category Name</Label>
          <Input id="name" name="name" value={formData.name} onChange={handleChange} required />
        </div>
        <div className="space-y-2">
          <Label htmlFor="image">Category Image</Label>
          <Input type="file" id="image" name="image" onChange={handleFileChange} />
          <p className="text-xs text-muted-foreground">Select an image file for the category</p>
        </div>
        <div className="flex items-center space-x-2">
          <Switch id="isActive" checked={formData.isActive} onCheckedChange={handleSwitchChange} />
          <Label htmlFor="isActive">Active</Label>
        </div>
        <div className="flex justify-end space-x-2 pt-4">
          <Button type="button" variant="outline" onClick={onClose}>
            Cancel
          </Button>
          <Button type="submit" disabled={isSubmitting}>
            {isSubmitting ? (isUpdateMode ? "Updating..." : "Creating...") : (isUpdateMode ? "Update Category" : "Create Category")}
          </Button>
        </div>
      </form>
    </ModalWrapper>
  );
}
