"use client"

import { useState } from "react"
import { Calendar, User, Eye, Edit, Trash2, FileText } from "lucide-react"
import { Card, CardContent, CardFooter, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import type { Post } from "@/types/post"
import Image from "next/image"

interface PostCardProps {
  post: Post
  onView: (post: Post) => void
  onEdit: (post: Post) => void
  onDelete: (post: Post) => void
}

export function PostCard({ post, onView, onEdit, onDelete }: PostCardProps) {
  const [imageError, setImageError] = useState(false)

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString()
  }

  const truncateContent = (content: string, maxLength = 150) => {
    if (content.length <= maxLength) return content
    return content.substring(0, maxLength) + "..."
  }

  return (
    <Card className="flex h-full flex-col">
      {post.imageList.length > 0 && !imageError ? (
        <div className="relative aspect-video overflow-hidden">
          <Image
            src={post.imageList[0] || "/placeholder.svg"}
            alt={post.title}
            width={450} height={250}
            className="h-full w-full object-cover transition-transform hover:scale-105"
            onError={() => setImageError(true)}
          />
        </div>
      ) : (
        <div className="flex aspect-video items-center justify-center bg-muted">
          <FileText className="h-12 w-12 text-muted-foreground" />
        </div>
      )}
      <CardHeader className="pb-2">
        <CardTitle className="line-clamp-2 text-lg">{post.title}</CardTitle>
        <div className="flex items-center justify-between text-xs text-muted-foreground">
          <div className="flex items-center gap-1">
            <User className="h-3 w-3" />
            <span>{post.authorName}</span>
          </div>
          <div className="flex items-center gap-1">
            <Calendar className="h-3 w-3" />
            <span>{formatDate(post.createdAt)}</span>
          </div>
        </div>
      </CardHeader>
      <CardContent className="flex-1">
        <p className="text-sm text-muted-foreground">{truncateContent(post.content)}</p>
      </CardContent>
      <CardFooter className="flex justify-between pt-2">
        <Button variant="ghost" size="sm" onClick={() => onView(post)}>
          <Eye className="mr-1 h-4 w-4" /> View
        </Button>
        <div className="flex gap-1">
          <Button variant="ghost" size="sm" onClick={() => onEdit(post)}>
            <Edit className="h-4 w-4" />
          </Button>
          <Button variant="ghost" size="sm" onClick={() => onDelete(post)} className="text-red-500 hover:text-red-700">
            <Trash2 className="h-4 w-4" />
          </Button>
        </div>
      </CardFooter>
    </Card>
  )
}