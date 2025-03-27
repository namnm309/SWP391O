"use client";

import { useState, useEffect } from "react";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogDescription,
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Checkbox } from "@/components/ui/checkbox";
import { Label } from "@/components/ui/label";
import { useStore } from "@/store";

interface CreateRoleModalProps {
  onClose: () => void;
}

export function CreateRoleModal({ onClose }: CreateRoleModalProps) {
  const { createRole, fetchPermissions } = useStore();
  const [name, setName] = useState("");
  const [description, setDescription] = useState("");
  const [availablePermissions, setAvailablePermissions] = useState<
    { name: string; description: string }[]
  >([]);
  const [selectedPermissions, setSelectedPermissions] = useState<string[]>([]);

  useEffect(() => {
    const loadPermissions = async () => {
      try {
        const permissions = await fetchPermissions();
        setAvailablePermissions(permissions);
      } catch (error) {
        console.error("Error fetching permissions:", error);
      }
    };
    loadPermissions();
  }, [fetchPermissions]);

  const handleCheckboxChange = (permissionName: string, checked: boolean) => {
    if (checked) {
      setSelectedPermissions((prev) => [...prev, permissionName]);
    } else {
      setSelectedPermissions((prev) => prev.filter((p) => p !== permissionName));
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await createRole({ name, description, permissions: selectedPermissions });
      onClose();
    } catch (error) {
      console.error("Error creating role:", error);
    }
  };

  return (
    <Dialog open onOpenChange={onClose}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Create Role</DialogTitle>
          <DialogDescription>
            Fill in the details to create a new role.
          </DialogDescription>
        </DialogHeader>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-sm font-medium">Name *</label>
            <Input value={name} onChange={(e) => setName(e.target.value)} required />
          </div>
          <div>
            <label className="block text-sm font-medium">Description *</label>
            <Input
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              required
            />
          </div>
          <div>
            <label className="block text-sm font-medium">Permissions</label>
            <div className="space-y-2">
              {availablePermissions.map((perm) => (
                <div key={perm.name} className="flex items-center space-x-2">
                  <Checkbox
                    checked={selectedPermissions.includes(perm.name)}
                    onCheckedChange={(checked) =>
                      handleCheckboxChange(perm.name, checked as boolean)
                    }
                  />
                  <Label>{perm.name}</Label>
                </div>
              ))}
            </div>
          </div>
          <div className="flex items-center justify-end space-x-2">
            <Button type="button" variant="outline" onClick={onClose}>
              Cancel
            </Button>
            <Button type="submit">Create Role</Button>
          </div>
        </form>
      </DialogContent>
    </Dialog>
  );
}
