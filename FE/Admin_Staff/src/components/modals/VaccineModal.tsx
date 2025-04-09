"use client";
import { useState } from "react";
import { useStore } from "@/store";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogDescription,
} from "@/components/ui/dialog";
import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import { Checkbox } from "@/components/ui/checkbox";

import type { Vaccine } from "@/types/vaccine";
import { useCategories } from "@/hooks/useCategories";

interface VaccineModalProps {
  onClose: () => void;
  vaccine?: Vaccine;
}

export function VaccineModal({ onClose, vaccine }: VaccineModalProps) {
  const { createVaccine, updateVaccine } = useStore();
  const { categories } = useCategories();

  const isUpdateMode = Boolean(vaccine);

  const [formData, setFormData] = useState({
    id: vaccine?.id || 0,
    title: vaccine?.title || "",
    categoryId: vaccine?.categoryId.toString() || "",
    price: vaccine?.price ?? 0,
    description: vaccine?.description || "",
    discount: vaccine?.discount ?? 0,
    discountPrice: vaccine?.discountPrice ?? 0,
    isActive: vaccine?.isActive ?? true,
    manufacturer: vaccine?.manufacturer || "",
    targetGroup: vaccine?.targetGroup || "",
    schedule: vaccine?.schedule || "",
    sideEffects: vaccine?.sideEffects || "",
    isPriority: vaccine?.isPriority || false,
    minAgeMonths: vaccine?.minAgeMonths ?? 0,
    maxAgeMonths: vaccine?.maxAgeMonths ?? 0,
    numberOfDoses: vaccine?.numberOfDoses ?? 1,
    minDaysBetweenDoses: vaccine?.minDaysBetweenDoses ?? 0,
    quantity: vaccine?.quantity ?? 0,
  });

  // For handling multiple images (FileList)
  const [images, setImages] = useState<FileList | null>(null);

  // Handle input changes
  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) => {
    const { name, value, type } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: type === "number" ? Number(value) : value,
    }));
  };

  // Handle select changes
  const handleSelectChange = (name: string, value: string) => {
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  // Handle checkbox changes
  const handleCheckboxChange = (name: string, checked: boolean) => {
    setFormData((prev) => ({
      ...prev,
      [name]: checked,
    }));
  };

  // Handle file input (multiple images)
  const handleImagesChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files) {
      setImages(e.target.files);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    const formDataToSubmit = new FormData();
    isUpdateMode && vaccine && formDataToSubmit.append("id", formData.id.toString());
    formDataToSubmit.append("title", formData.title);
    formDataToSubmit.append("categoryId", formData.categoryId);
    formDataToSubmit.append("price", formData.price.toString());
    formDataToSubmit.append("quantity", formData.quantity.toString());
    formDataToSubmit.append("description", formData.description);
    formDataToSubmit.append("discount", formData.discount.toString());
    formDataToSubmit.append("discountPrice", formData.discountPrice.toString());
    formDataToSubmit.append("isActive", JSON.stringify(formData.isActive));
    formDataToSubmit.append("manufacturer", formData.manufacturer);
    formDataToSubmit.append("targetGroup", formData.targetGroup);
    formDataToSubmit.append("schedule", formData.schedule);
    formDataToSubmit.append("sideEffects", formData.sideEffects);
    formDataToSubmit.append("available", JSON.stringify(formData.isActive));
    formDataToSubmit.append("isPriority", JSON.stringify(formData.isPriority));
    formDataToSubmit.append("minAgeMonths", formData.minAgeMonths.toString());
    formDataToSubmit.append("maxAgeMonths", formData.maxAgeMonths.toString());
    formDataToSubmit.append(
      "numberOfDoses",
      formData.numberOfDoses.toString()
    );
    formDataToSubmit.append(
      "minDaysBetweenDoses",
      formData.minDaysBetweenDoses.toString()
    );

    // Append images if available
    if (images) {
      for (let i = 0; i < images.length; i++) {
        formDataToSubmit.append("images", images[i]);
      }
    }

    try {
      if (isUpdateMode && vaccine) {
        await updateVaccine(vaccine.id, formDataToSubmit);
      } else {
        await createVaccine(formDataToSubmit);
      }
      onClose();
    } catch (error) {
      console.error("Error submitting vaccine form:", error);
    }
  };

  return (
    <Dialog open onOpenChange={onClose}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>
            {isUpdateMode ? "Update Vaccine" : "Create Vaccine"}
          </DialogTitle>
          <DialogDescription>
            {isUpdateMode
              ? "Modify the vaccine details below."
              : "Fill in the details below to create a new vaccine."}
          </DialogDescription>
        </DialogHeader>

        {/* Form with 3-column layout */}
        <form onSubmit={handleSubmit} className="grid grid-cols-3 gap-6">
          {/* Column 1 */}
          <div className="flex flex-col space-y-4 col-span-1">
            {/* Vaccine Name */}
            <div className="space-y-2">
              <Label htmlFor="title">Vaccine Name</Label>
              <Input
                id="title"
                name="title"
                value={formData.title}
                onChange={handleChange}
                required
              />
            </div>

            {/* Category */}
            <div className="space-y-2">
              <Label htmlFor="categoryId">Category</Label>
              <Select
                name="categoryId"
                value={formData.categoryId}
                onValueChange={(val) => handleSelectChange("categoryId", val)}
              >
                <SelectTrigger className="w-full">
                  <SelectValue placeholder="Select category" />
                </SelectTrigger>
                <SelectContent>
                  {categories?.map((category) => {
                    if (category.subCategories && category.subCategories.length > 0) {
                      return (
                        <SelectGroup key={category.id}>
                          <SelectItem
                            key={category.id}
                            value={category.id.toString()}
                          >
                            {category.name}
                          </SelectItem>

                          {category.subCategories.map((sub) => (
                            <SelectItem
                              key={sub.id}
                              value={sub.id.toString()}
                              className="pl-6"
                            >
                              &nbsp;&nbsp;{sub.name}
                            </SelectItem>
                          ))}
                        </SelectGroup>
                      )
                    }

                    return (
                      <SelectItem
                        key={category.id}
                        value={category.id.toString()}
                      >
                        {category.name}
                      </SelectItem>
                    )
                  })}
                </SelectContent>
              </Select>
            </div>

            {/* Manufacturer */}
            <div className="space-y-2">
              <Label htmlFor="manufacturer">Manufacturer</Label>
              <Input
                id="manufacturer"
                name="manufacturer"
                value={formData.manufacturer}
                onChange={handleChange}
                required
              />
            </div>

            {/* Images (Multiple) */}
            <div className="space-y-2">
              <Label htmlFor="images">Images</Label>
              <Input
                id="images"
                name="images"
                type="file"
                multiple
                onChange={handleImagesChange}
              />
            </div>
          </div>

          {/* Column 2 */}
          <div className="flex flex-col space-y-4 col-span-1">
            {/* Price */}
            <div className="space-y-2">
              <Label htmlFor="price">Price</Label>
              <Input
                id="price"
                name="price"
                type="number"
                min="0"
                step="0.01"
                value={formData.price}
                onChange={handleChange}
                required
              />
            </div>

            {/* Discount */}
            <div className="space-y-2">
              <Label htmlFor="discount">Discount (%)</Label>
              <Input
                id="discount"
                name="discount"
                type="number"
                min="0"
                max="100"
                value={formData.discount}
                onChange={handleChange}
                required
              />
            </div>

            {/* Quantity */}
            <div className="space-y-2">
              <Label htmlFor="quantity">Quantity</Label>
              <Input
                id="quantity"
                name="quantity"
                type="number"
                min="0"
                value={formData.quantity}
                onChange={handleChange}
                required
              />
            </div>

            {/* Number of Doses */}
            <div className="space-y-2">
              <Label htmlFor="numberOfDoses">Number of Doses</Label>
              <Input
                id="numberOfDoses"
                name="numberOfDoses"
                type="number"
                min="1"
                value={formData.numberOfDoses}
                onChange={handleChange}
                required
              />
            </div>

            {/* Min Age */}
            <div className="space-y-2">
              <Label htmlFor="minAgeMonths">Min Age (months)</Label>
              <Input
                id="minAgeMonths"
                name="minAgeMonths"
                type="number"
                min="0"
                value={formData.minAgeMonths}
                onChange={handleChange}
                required
              />
            </div>

            {/* Max Age */}
            <div className="space-y-2">
              <Label htmlFor="maxAgeMonths">Max Age (months)</Label>
              <Input
                id="maxAgeMonths"
                name="maxAgeMonths"
                type="number"
                min="0"
                value={formData.maxAgeMonths}
                onChange={handleChange}
                required
              />
            </div>

            {/* Min Days Between Doses */}
            <div className="space-y-2">
              <Label htmlFor="minDaysBetweenDoses">
                Min Days Between Doses
              </Label>
              <Input
                id="minDaysBetweenDoses"
                name="minDaysBetweenDoses"
                type="number"
                min="0"
                value={formData.minDaysBetweenDoses}
                onChange={handleChange}
                required
              />
            </div>
          </div>

          {/* Column 3 */}
          <div className="flex flex-col space-y-4 col-span-1">
            {/* Target Group */}
            <div className="space-y-2">
              <Label htmlFor="targetGroup">Target Group</Label>
              <Input
                id="targetGroup"
                name="targetGroup"
                value={formData.targetGroup}
                onChange={handleChange}
                required
              />
            </div>

            {/* Description */}
            <div className="space-y-2">
              <Label htmlFor="description">Description</Label>
              <Textarea
                id="description"
                name="description"
                value={formData.description}
                onChange={handleChange}
                rows={3}
                required
              />
            </div>

            {/* Schedule */}
            <div className="space-y-2">
              <Label htmlFor="schedule">Schedule</Label>
              <Textarea
                id="schedule"
                name="schedule"
                value={formData.schedule}
                onChange={handleChange}
                rows={2}
                placeholder="E.g., First dose at 2 months, second dose at 4 months..."
                required
              />
            </div>

            {/* Side Effects */}
            <div className="space-y-2">
              <Label htmlFor="sideEffects">Side Effects</Label>
              <Textarea
                id="sideEffects"
                name="sideEffects"
                value={formData.sideEffects}
                onChange={handleChange}
                rows={2}
                required
              />
            </div>

            {/* Checkboxes */}
            <div className="space-y-2">
              <div className="flex items-center space-x-2">
                <Checkbox
                  id="isActive"
                  checked={formData.isActive}
                  onCheckedChange={(checked) =>
                    handleCheckboxChange("isActive", checked as boolean)
                  }
                />
                <Label htmlFor="isActive" className="cursor-pointer">
                  Active
                </Label>
              </div>

              <div className="flex items-center space-x-2">
                <Checkbox
                  id="isPriority"
                  checked={formData.isPriority}
                  onCheckedChange={(checked) =>
                    handleCheckboxChange("isPriority", checked as boolean)
                  }
                />
                <Label htmlFor="isPriority" className="cursor-pointer">
                  Priority
                </Label>
              </div>
            </div>
          </div>

          {/* Action buttons (across all columns) */}
          <div className="col-span-3 flex justify-end space-x-2 pt-4">
            <Button type="button" variant="outline" onClick={onClose}>
              Cancel
            </Button>
            <Button type="submit">
              {isUpdateMode ? "Update" : "Create"} Vaccine
            </Button>
          </div>
        </form>
      </DialogContent>
    </Dialog>
  );
}
