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
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import type { Vaccine } from "@/types/vaccine";
import type { Category } from "@/types/category";
import { useCategories } from "@/hooks/useCategories";

interface VaccineModalProps {
  onClose: () => void;
  vaccine?: Vaccine;
}

export function VaccineModal({ onClose, vaccine }: VaccineModalProps) {
  const { createVaccine, updateVaccine } = useStore();
  const { categories, loading } = useCategories();

  const isUpdateMode = Boolean(vaccine);

  const [title, setTitle] = useState(vaccine?.title || "");
  const [selectedCategoryId, setSelectedCategoryId] = useState<string>(
    vaccine && vaccine.id ? vaccine.id.toString() : ""
  );
  const [price, setPrice] = useState<number>(vaccine?.price || 0);
  const [stock, setStock] = useState<number>(vaccine?.stock || 0);
  const [description, setDescription] = useState(vaccine?.description || "");

  const [discount, setDiscount] = useState<number>(vaccine?.discount || 0);
  const [discountPrice, setDiscountPrice] = useState<number>(vaccine?.discountPrice || 0);
  const [manufacturer, setManufacturer] = useState(vaccine?.manufacturer || "");
  const [targetGroup, setTargetGroup] = useState(vaccine?.targetGroup || "");
  const [schedule, setSchedule] = useState(vaccine?.schedule || "");
  const [sideEffects, setSideEffects] = useState(vaccine?.sideEffects || "");
  const [isActive, setIsActive] = useState<boolean>(vaccine?.isActive ?? true);
  const [available, setAvailable] = useState<boolean>(vaccine?.available ?? true);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    const formData = new FormData();
    formData.append("title", title);
    if (selectedCategoryId) {
      formData.append("categoryId", selectedCategoryId);
    }
    formData.append("price", price.toString());
    formData.append("stock", stock.toString());
    formData.append("description", description);
    formData.append("discount", discount.toString());
    formData.append("discountPrice", discountPrice.toString());
    formData.append("manufacturer", manufacturer);
    formData.append("targetGroup", targetGroup);
    formData.append("schedule", schedule);
    formData.append("sideEffects", sideEffects);
    formData.append("isActive", JSON.stringify(isActive));
    formData.append("available", JSON.stringify(available));

    try {
      if (isUpdateMode && vaccine) {
        await updateVaccine(vaccine.id, formData);
      } else {
        await createVaccine(formData);
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
          <DialogTitle>{isUpdateMode ? "Update Vaccine" : "Create Vaccine"}</DialogTitle>
          <DialogDescription>
            {isUpdateMode
              ? "Modify the vaccine details below."
              : "Fill in the details below to create a new vaccine."}
          </DialogDescription>
        </DialogHeader>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="grid grid-cols-2 gap-4">
            {/* Left Column */}
            <div className="space-y-4">
              <div>
                <label className="block text-sm font-medium">Title *</label>
                <Input value={title} onChange={(e) => setTitle(e.target.value)} required />
              </div>
              <div>
                <label className="block text-sm font-medium">Category *</label>
                <Select value={selectedCategoryId} onValueChange={(value) => setSelectedCategoryId(value)}>
                  <SelectTrigger className="w-full">
                    <SelectValue placeholder="Select category" />
                  </SelectTrigger>
                  <SelectContent>
                    {categories.map((category: Category) => (
                      <SelectItem key={category.id} value={category.id.toString()}>
                        {category.name}
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>
              </div>
              <div>
                <label className="block text-sm font-medium">Price *</label>
                <Input
                  type="number"
                  value={price}
                  onChange={(e) => setPrice(parseFloat(e.target.value))}
                  required
                />
              </div>
              <div>
                <label className="block text-sm font-medium">Stock *</label>
                <Input
                  type="number"
                  value={stock}
                  onChange={(e) => setStock(parseInt(e.target.value))}
                  required
                />
              </div>
              <div>
                <label className="block text-sm font-medium">Description *</label>
                <textarea
                  className="w-full rounded-md border border-gray-300 p-2"
                  value={description}
                  onChange={(e) => setDescription(e.target.value)}
                  required
                  rows={7}
                />
              </div>
            </div>
            {/* Right Column */}
            <div className="space-y-4">
              <div>
                <label className="block text-sm font-medium">Discount *</label>
                <Input
                  type="number"
                  value={discount}
                  onChange={(e) => setDiscount(parseInt(e.target.value))}
                  required
                />
              </div>
              <div>
                <label className="block text-sm font-medium">Discount Price *</label>
                <Input
                  type="number"
                  value={discountPrice}
                  onChange={(e) => setDiscountPrice(parseFloat(e.target.value))}
                  required
                />
              </div>
              <div>
                <label className="block text-sm font-medium">Manufacturer *</label>
                <Input value={manufacturer} onChange={(e) => setManufacturer(e.target.value)} required />
              </div>
              <div>
                <label className="block text-sm font-medium">Target Group *</label>
                <Input value={targetGroup} onChange={(e) => setTargetGroup(e.target.value)} required />
              </div>
              <div>
                <label className="block text-sm font-medium">Schedule *</label>
                <Input value={schedule} onChange={(e) => setSchedule(e.target.value)} required />
              </div>
              <div>
                <label className="block text-sm font-medium">Side Effects *</label>
                <Input value={sideEffects} onChange={(e) => setSideEffects(e.target.value)} required />
              </div>
              {/* Bottom Section: Boolean fields */}
              <div className="flex flex-col space-y-2 mt-4">
                {/* <div>
                  <label className="block text-sm font-medium">Active *</label>
                  <select
                    className="w-full rounded-md border border-gray-300 p-2"
                    value={isActive ? "true" : "false"}
                    onChange={(e) => setIsActive(e.target.value === "true")}
                  >
                    <option value="true">Active</option>
                    <option value="false">Inactive</option>
                  </select>
                </div> */}
                <div>
                  <label className="block text-sm font-medium">Available *</label>
                  <select
                    className="w-full rounded-md border border-gray-300 p-2"
                    value={available ? "true" : "false"}
                    onChange={(e) => setAvailable(e.target.value === "true")}
                  >
                    <option value="true">Yes</option>
                    <option value="false">No</option>
                  </select>
                </div>
              </div>
            </div>
          </div>
          <div className="flex items-center justify-end space-x-2">
            <Button type="button" variant="outline" onClick={onClose}>
              Cancel
            </Button>
            <Button type="submit">{isUpdateMode ? "Update Vaccine" : "Create Vaccine"}</Button>
          </div>
        </form>
      </DialogContent>
    </Dialog>
  );
}
