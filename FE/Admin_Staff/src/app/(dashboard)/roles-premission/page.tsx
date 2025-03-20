"use client";
import React, { useState, useEffect, useCallback } from "react";
import { Edit, ShieldCheck, Check, X, Plus } from "lucide-react";
import { RoleUser } from "@/types/enums";
import { useStore } from "@/store";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { useToast } from "@/hooks/use-toast";
import { Badge } from "@/components/ui/badge";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table";
import { CreatePermissionModal } from "@/components/modals/CreatePermissionModal";
import { CreateRoleModal } from "@/components/modals/CreateRoleModal";
import { VaccinePreview } from "@/components/modals/VaccinePreview"; // if needed for preview
import type { Role, Permission } from "@/types/management";

export default function RolesPage() {
  const { user } = useStore.getState().profile;
  const { fetchRoles, fetchPermissions, deleteRole, deletePermission } = useStore.getState();
  const { toast } = useToast();

  const [roles, setRoles] = useState<Role[]>([]);
  const [permissions, setPermissions] = useState<Permission[]>([]);
  const [loading, setLoading] = useState(true);
  const [activeTab, setActiveTab] = useState("roles");

  const [isCreateRoleModalOpen, setIsCreateRoleModalOpen] = useState(false);
  const [isCreatePermissionModalOpen, setIsCreatePermissionModalOpen] = useState(false);

  const loadData = useCallback(async () => {
    try {
      setLoading(true);
      const [rolesData, permissionsData] = await Promise.all([
        fetchRoles(),
        fetchPermissions(),
      ]);
      setRoles(rolesData);
      setPermissions(permissionsData);
    } catch (error) {
      toast({
        title: "Error",
        description: "Failed to load roles and permissions",
        variant: "destructive",
      });
    } finally {
      setLoading(false);
    }
  }, [fetchRoles, fetchPermissions, toast]);

  useEffect(() => {
    loadData();
  }, [loadData]);

  const handleEditRole = (roleId: number) => {
    // Implement editing logic here if needed.
  };

  const handleDeleteRole = async (roleName: string) => {
    try {
      await deleteRole(roleName);
      toast({
        title: "Success",
        description: "Role deleted successfully",
      });
      // After deletion, refetch data
      await loadData();
    } catch (error) {
      toast({
        title: "Error",
        description: "Failed to delete role",
        variant: "destructive",
      });
    }
  };

  const handleDeletePermission = async (roleName: string) => {
    try {
      await deletePermission(roleName);
      toast({
        title: "Success",
        description: "Permission deleted successfully",
      });
      await loadData();
    } catch (error) {
      toast({
        title: "Error",
        description: "Failed to delete role",
        variant: "destructive",
      });
    }
  };

  // const permissionsByModule = roles.reduce((acc, role) => {
  //   if (role.permissions) {
  //     role.permissions.forEach((perm) => {
  //       if (!acc[perm.module]) {
  //         acc[perm.module] = [];
  //       }
  //       acc[perm.module].push(perm);
  //     });
  //   }
  //   return acc;
  // }, {} as Record<string, Permission[]>);

  if (user?.role !== RoleUser.ADMIN) {
    return (
      <div className="flex h-[calc(100vh-16rem)] flex-col items-center justify-center">
        <h1 className="text-2xl font-bold">Access Denied</h1>
        <p className="text-muted-foreground">
          You don't have permission to access this page.
        </p>
      </div>
    );
  }

  return (
    <div className="space-y-4">
      {/* Header with Create Role and Create Permission buttons */}
      <div className="flex items-center justify-between">
        <h1 className="text-2xl font-bold">Roles & Permissions</h1>
        <div className="flex space-x-2">
          <Button onClick={() => setIsCreateRoleModalOpen(true)}>
            <ShieldCheck className="mr-2 h-4 w-4" />
            Add Role
          </Button>
          <Button onClick={() => setIsCreatePermissionModalOpen(true)}>
            <ShieldCheck className="mr-2 h-4 w-4" />
            Add Permission
          </Button>
        </div>
      </div>

      <Tabs value={activeTab} onValueChange={setActiveTab}>
        <TabsList>
          <TabsTrigger value="roles">Roles</TabsTrigger>
          <TabsTrigger value="permissions">Permissions</TabsTrigger>
          <TabsTrigger value="matrix">Permissions Matrix</TabsTrigger>
        </TabsList>

        {/* Roles Tab */}
        <TabsContent value="roles" className="space-y-4">
          {loading ? (
            <div className="flex h-40 items-center justify-center">
              <p>Loading roles...</p>
            </div>
          ) : (
            <div className="grid gap-4 md:grid-cols-3">
              {roles.map((role, idx) => (
                <Card key={idx}>
                  <CardHeader className="pb-2">
                    <div className="flex items-center justify-between">
                      <CardTitle className="text-lg font-medium">
                        {role.name}
                      </CardTitle>
                      <div className="flex space-x-1">
                        {/* <Button
                          variant="ghost"
                          size="sm"
                          onClick={() => handleEditRole(role.id)}
                        >
                          <Edit className="h-4 w-4" />
                        </Button> */}
                        <Button
                          variant="ghost"
                          size="sm"
                          onClick={() => handleDeleteRole(role.name)}
                        >
                          <X className="h-4 w-4 text-red-500" />
                        </Button>
                      </div>
                    </div>
                  </CardHeader>
                  <CardContent>
                    <p className="text-sm text-muted-foreground mb-4">
                      {role.description}
                    </p>
                    {/* <div className="flex items-center justify-between">
                      <Badge variant="outline">
                        {role.users_count} users
                      </Badge>
                      <span className="text-xs text-muted-foreground">
                        Created: {new Date(role.created_at).toLocaleDateString()}
                      </span>
                    </div> */}
                  </CardContent>
                </Card>
              ))}
            </div>
          )}
        </TabsContent>

        {/* Permissions Tab */}
        <TabsContent value="permissions" className="space-y-4">
          {loading ? (
            <div className="flex h-40 items-center justify-center">
              <p>Loading permissions...</p>
            </div>
          ) : (
            <div className="grid gap-4 md:grid-cols-3">
              {permissions.map((permission) => (
                <Card key={permission.id}>
                  <CardHeader>
                    <div className="flex items-center justify-between">
                      <CardTitle>{permission.name}</CardTitle>
                      <div className="flex space-x-1">
                        <Button
                          variant="ghost"
                          size="sm"
                          onClick={() => handleDeletePermission(permission.name)}
                        >
                          <X className="h-4 w-4 text-red-500" />
                        </Button>
                      </div>
                    </div>
                  </CardHeader>
                  <CardContent>
                    <p className="text-sm text-muted-foreground">
                      {permission.description}
                    </p>
                  </CardContent>
                </Card>
              ))}
            </div>
          )}
        </TabsContent>

        {/* Permissions Matrix Tab */}
        <TabsContent value="matrix">
          <Card>
            <CardHeader>
              <CardTitle>Permissions Matrix</CardTitle>
            </CardHeader>
            <CardContent>
              {loading ? (
                <div className="flex h-40 items-center justify-center">
                  <p>Loading matrix...</p>
                </div>
              ) : (
                <div className="overflow-x-auto">
                  <Table>
                    <TableHeader>
                      <TableRow>
                        <TableHead className="w-[250px]">Permission</TableHead>
                        {roles.map((role) => (
                          <TableHead key={role.id} className="text-center">
                            {role.name}
                          </TableHead>
                        ))}
                      </TableRow>
                    </TableHeader>
                    <TableBody>
                      {(() => {
                        // Build unique list of permissions from all roles.
                        const permissionMap = new Map<string, Permission>();
                        roles.forEach((role) => {
                          role.permissions?.forEach((perm) => {
                            if (!permissionMap.has(perm.name)) {
                              permissionMap.set(perm.name, perm);
                            }
                          });
                        });
                        const allPermissions = Array.from(permissionMap.values());
                        return allPermissions.map((permission, idx) => (
                          <TableRow key={idx}>
                            <TableCell className="font-medium">
                              <div className="flex flex-col">
                                <span>{permission.name}</span>
                                <span className="text-xs text-muted-foreground">
                                  {permission.description}
                                </span>
                              </div>
                            </TableCell>
                            {roles.map((role) => (
                              <TableCell key={role.id} className="text-center">
                                {role.permissions?.some(
                                  (p) => p.name === permission.name
                                ) ? (
                                  <Check className="mx-auto h-5 w-5 text-green-500" />
                                ) : (
                                  <X className="mx-auto h-5 w-5 text-red-500" />
                                )}
                              </TableCell>
                            ))}
                          </TableRow>
                        ));
                      })()}
                    </TableBody>
                  </Table>
                </div>
              )}
            </CardContent>
          </Card>
        </TabsContent>
      </Tabs>

      {/* Modals */}
      {isCreateRoleModalOpen && (
        <CreateRoleModal
          onClose={async () => {
            setIsCreateRoleModalOpen(false);
            await loadData();
          }}
        />
      )}
      {isCreatePermissionModalOpen && (
        <CreatePermissionModal
          onClose={async () => {
            setIsCreatePermissionModalOpen(false);
            await loadData();
          }}
        />
      )}
    </div>
  );
}
