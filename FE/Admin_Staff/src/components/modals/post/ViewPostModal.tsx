"use client"
import Image from "next/image"
import { User, Clock } from "lucide-react"
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogDescription,
} from "@/components/ui/dialog"
import { Separator } from "@/components/ui/separator"
import type { Post } from "@/types/post"
import { Button } from "@/components/ui/button"

interface ViewPostModalProps {
  post: Post;
  onClose: () => void;
}

export function ViewPostModal({ post, onClose }: ViewPostModalProps) {
  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString("en-US", {
      year: "numeric",
      month: "long",
      day: "numeric",
      hour: "2-digit",
      minute: "2-digit",
    })
  }

  return (
    <Dialog open onOpenChange={onClose}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>{post.title}</DialogTitle>
          <DialogDescription>View detailed information about this post.</DialogDescription>
        </DialogHeader>

        <div className="space-y-4">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-2">
              <User className="h-4 w-4 text-gray-500" />
              <span className="text-sm text-gray-500">{post.authorName}</span>
            </div>
            <div className="flex items-center gap-2">
              <Clock className="h-4 w-4 text-gray-500" />
              <span className="text-sm text-gray-500">{formatDate(post.createdAt)}</span>
            </div>
          </div>

          <Separator />

          <div className="whitespace-pre-wrap text-sm">{post.content}</div>

          <Separator />

          <div className="whitespace-pre-wrap text-sm">{post.maincontent}</div>

          {post.imageList.length > 0 && (
            <>
              <Separator />
              <div className="space-y-2">
                <h3 className="text-sm font-medium">Images</h3>
                <div className="grid grid-cols-2 gap-2">
                  {post.imageList.map((image, index) => (
                    <div key={index} className="relative aspect-video overflow-hidden rounded-md border">
                      <Image
                        src={image || "/placeholder.svg"}
                        alt={`Image ${index + 1}`}
                        width={450} height={250}
                        className="h-full w-full object-cover"
                        onError={(e) => {
                          e.currentTarget.src = "/placeholder.svg?height=200&width=300"
                        }}
                      />
                    </div>
                  ))}
                </div>
              </div>
            </>
          )}

          {post.updatedAt !== post.createdAt && (
            <div className="text-xs text-gray-500">Last updated: {formatDate(post.updatedAt)}</div>
          )}

          <div className="flex justify-end space-x-2 pt-4">
            <Button variant="outline" onClick={onClose}>
              Close
            </Button>
          </div>
        </div>
      </DialogContent>
    </Dialog>
  )
}
