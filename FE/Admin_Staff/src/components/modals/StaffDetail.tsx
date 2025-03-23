"use client"

import { Mail, Phone, Calendar, User, Shield, Key, Lock, Unlock, CheckCircle, XCircle } from 'lucide-react'
import { Dialog, DialogContent, DialogHeader, DialogTitle } from "@/components/ui/dialog"
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar"
import { Badge } from "@/components/ui/badge"
import { Button } from "@/components/ui/button"
import { Separator } from "@/components/ui/separator"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import type { StaffMember } from "@/types/staff"
import { useToast } from "@/hooks/use-toast";

interface StaffDetailsModalProps {
  isOpen: boolean
  onClose: () => void
  staff: StaffMember | null
}

export function StaffDetailsModal({ isOpen, onClose, staff }: StaffDetailsModalProps) {
  const { toast } = useToast()

  if (!staff) return null

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    })
  }

  const handleResetPassword = () => {
    toast({
      title: "Password Reset Link Sent",
      description: `A password reset link has been sent to ${staff.email}`,
    })
  }

  const handleToggleStatus = () => {
    toast({
      title: "Status Updated",
      description: `Staff member has been ${staff.enabled ? 'deactivated' : 'activated'}`,
    })
  }

  const handleToggleLock = () => {
    toast({
      title: "Account Updated",
      description: `Account has been ${staff.accountNonLocked ? 'locked' : 'unlocked'}`,
    })
  }

  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent className="max-w-3xl">
        <DialogHeader>
          <DialogTitle>Staff Details</DialogTitle>
        </DialogHeader>
        
        <div className="space-y-6">
          {/* Staff Profile Header */}
          <div className="flex items-center gap-4">
            <Avatar className="h-16 w-16">
              <AvatarImage src={staff.avatarUrl || ""} alt={staff.fullname} />
              <AvatarFallback>
                {staff.fullname
                  .split(" ")
                  .map((n) => n[0])
                  .join("")
                  .toUpperCase()}
              </AvatarFallback>
            </Avatar>
            <div>
              <h2 className="text-xl font-semibold">{staff.fullname}</h2>
              <p className="text-sm text-gray-500">@{staff.username}</p>
              <div className="mt-1 flex gap-2">
                {staff.roles.map((role, index) => (
                  <Badge key={index} variant="outline">
                    {role.name.replace('ROLE_', '')}
                  </Badge>
                ))}
                {staff.enabled ? 
                  <Badge className="bg-green-100 text-green-800">Active</Badge> : 
                  <Badge className="bg-red-100 text-red-800">Inactive</Badge>
                }
                {staff.accountNonLocked ? 
                  <Badge className="bg-blue-100 text-blue-800">Unlocked</Badge> : 
                  <Badge className="bg-yellow-100 text-yellow-800">Locked</Badge>
                }
              </div>
            </div>
          </div>
          
          <Tabs defaultValue="profile">
            <TabsList className="grid w-full grid-cols-2">
              <TabsTrigger value="profile">Profile</TabsTrigger>
              <TabsTrigger value="roles">Roles & Permissions</TabsTrigger>
              {/* <TabsTrigger value="actions">Account Actions</TabsTrigger> */}
            </TabsList>
            
            <TabsContent value="profile" className="space-y-4 pt-4">
              <div className="grid gap-4 md:grid-cols-2">
                <div className="space-y-1">
                  <div className="flex items-center gap-2 text-sm text-gray-500">
                    <Mail className="h-4 w-4" />
                    <span>Email</span>
                  </div>
                  <p className="font-medium">{staff.email}</p>
                </div>
                
                <div className="space-y-1">
                  <div className="flex items-center gap-2 text-sm text-gray-500">
                    <Phone className="h-4 w-4" />
                    <span>Phone</span>
                  </div>
                  <p className="font-medium">{staff.phone}</p>
                </div>
                
                <div className="space-y-1">
                  <div className="flex items-center gap-2 text-sm text-gray-500">
                    <Calendar className="h-4 w-4" />
                    <span>Date of Birth</span>
                  </div>
                  <p className="font-medium">{formatDate(staff.bod)}</p>
                </div>
                
                <div className="space-y-1">
                  <div className="flex items-center gap-2 text-sm text-gray-500">
                    <User className="h-4 w-4" />
                    <span>Gender</span>
                  </div>
                  <p className="font-medium">{staff.gender}</p>
                </div>
              </div>
              
              <Separator />
              
              <div className="grid gap-4 md:grid-cols-2">
                <div className="space-y-1">
                  <div className="text-sm text-gray-500">Height</div>
                  <p className="font-medium">{staff.height} cm</p>
                </div>
                
                <div className="space-y-1">
                  <div className="text-sm text-gray-500">Weight</div>
                  <p className="font-medium">{staff.weight} kg</p>
                </div>
              </div>
            </TabsContent>
            
            <TabsContent value="roles" className="space-y-4 pt-4">
              {staff.roles.map((role, index) => (
                <Card key={index}>
                  <CardHeader>
                    <CardTitle>{role.name.replace('ROLE_', '')}</CardTitle>
                    <CardDescription>{role.description}</CardDescription>
                  </CardHeader>
                  <CardContent>
                    <h4 className="mb-2 font-medium">Permissions:</h4>
                    <div className="flex flex-wrap gap-2">
                      {role.permissions.map((permission, idx) => (
                        <div key={idx} className="rounded-md border p-2">
                          <div className="font-medium">{permission.name}</div>
                          <div className="text-xs text-gray-500">{permission.description}</div>
                        </div>
                      ))}
                    </div>
                  </CardContent>
                </Card>
              ))}
              
              {staff.roles.length === 0 && (
                <div className="rounded-md border border-dashed p-4 text-center text-gray-500">
                  No roles assigned to this staff member
                </div>
              )}
            </TabsContent>
            
            {/* <TabsContent value="actions" className="space-y-4 pt-4">
              <div className="grid gap-4 md:grid-cols-2">
                <Card>
                  <CardHeader>
                    <CardTitle>Account Status</CardTitle>
                    <CardDescription>Activate or deactivate this staff member</CardDescription>
                  </CardHeader>
                  <CardContent>
                    <Button 
                      onClick={handleToggleStatus}
                      className={staff.enabled ? "bg-red-500 hover:bg-red-600" : "bg-green-500 hover:bg-green-600"}
                    >
                      {staff.enabled ? (
                        <>
                          <XCircle className="mr-2 h-4 w-4" />
                          Deactivate Account
                        </>
                      ) : (
                        <>
                          <CheckCircle className="mr-2 h-4 w-4" />
                          Activate Account
                        </>
                      )}
                    </Button>
                  </CardContent>
                </Card>
                
                <Card>
                  <CardHeader>
                    <CardTitle>Account Lock</CardTitle>
                    <CardDescription>Lock or unlock this staff member's account</CardDescription>
                  </CardHeader>
                  <CardContent>
                    <Button 
                      onClick={handleToggleLock}
                      className={staff.accountNonLocked ? "bg-yellow-500 hover:bg-yellow-600" : "bg-blue-500 hover:bg-blue-600"}
                    >
                      {staff.accountNonLocked ? (
                        <>
                          <Lock className="mr-2 h-4 w-4" />
                          Lock Account
                        </>
                      ) : (
                        <>
                          <Unlock className="mr-2 h-4 w-4" />
                          Unlock Account
                        </>
                      )}
                    </Button>
                  </CardContent>
                </Card>
                
                <Card className="md:col-span-2">
                  <CardHeader>
                    <CardTitle>Password Reset</CardTitle>
                    <CardDescription>Send a password reset link to this staff member</CardDescription>
                  </CardHeader>
                  <CardContent>
                    <Button onClick={handleResetPassword}>
                      <Key className="mr-2 h-4 w-4" />
                      Send Password Reset Link
                    </Button>
                  </CardContent>
                </Card>
              </div>
            </TabsContent> */}
          </Tabs>
        </div>
      </DialogContent>
    </Dialog>
  )
}
