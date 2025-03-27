"use client"

import type React from "react"
import { X } from "lucide-react"
import { Card, CardContent, CardFooter, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"

interface PreviewCardProps {
  title: string
  onClose: () => void
  onEdit?: () => void
  children: React.ReactNode
  className?: string
  footer?: React.ReactNode
}

export function PreviewCard({ title, onClose, onEdit, children, className, footer }: PreviewCardProps) {
  return (
    <Card className={`w-full max-w-md shadow-lg ${className}`}>
      <CardHeader className="flex flex-row items-center justify-between pb-2">
        <CardTitle className="text-xl font-semibold">{title}</CardTitle>
        <Button variant="ghost" size="icon" onClick={onClose} className="h-8 w-8">
          <X className="h-4 w-4" />
        </Button>
      </CardHeader>
      <CardContent>{children}</CardContent>
      {(footer || onEdit) && (
        <CardFooter className="flex justify-end gap-2 pt-2">
          {footer}
          {onEdit && (
            <Button onClick={onEdit} variant="outline" size="sm">
              Edit
            </Button>
          )}
        </CardFooter>
      )}
    </Card>
  )
}

