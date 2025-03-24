"use client"
import { useState, useEffect } from "react"
import { useToast } from "@/hooks/use-toast"
import { ModalWrapper } from "@/components/ui/modal-wrapper"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Textarea } from "@/components/ui/textarea"
import { Post } from "@/types/post"
import Image from "next/image"

interface CreateEditPostModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSubmit: (formData: FormData, postId?: number) => Promise<void>;
  post?: Post | null;
}

export function CreateEditPostModal({
  isOpen,
  onClose,
  onSubmit,
  post,
}: CreateEditPostModalProps) {
  const { toast } = useToast()
  const isUpdateMode = Boolean(post)

  const [title, setTitle] = useState("")
  const [content, setContent] = useState("")
  const [mainContent, setMainContent] = useState("")
  const [images, setImages] = useState<File[]>([])
  const [existingImages, setExistingImages] = useState<string[]>([])
  const [removedImages, setRemovedImages] = useState<string[]>([])
  const [isSubmitting, setIsSubmitting] = useState(false)

  useEffect(() => {
    if (post) {
      setTitle(post.title)
      setContent(post.content)
      setMainContent(post.maincontent ?? "")
      setExistingImages(post.imageList ?? [])
      setRemovedImages([])
    } else {
      setTitle("")
      setContent("")
      setMainContent("")
      setExistingImages([])
      setRemovedImages([])
    }
    setImages([])
  }, [post])

  const handleAddImages = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (!e.target.files) return;
    setImages((prev) => [...prev, ...Array.from(e.target.files!)]);
  }

  const handleRemoveNewImage = (index: number) => {
    setImages((prev) => prev.filter((_, i) => i !== index))
  }

  const handleRemoveExistingImage = (index: number) => {
    setExistingImages((prev) => {
      const removed = prev[index]
      setRemovedImages((old) => [...old, removed])
      return prev.filter((_, i) => i !== index)
    })
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setIsSubmitting(true)
    try {
      const fd = new FormData()
      fd.append("title", title)
      fd.append("content", content)
      fd.append("maincontent", mainContent)

      images.forEach((file) => {
        fd.append("file", file)
      })

      if (isUpdateMode && removedImages.length > 0) {
        fd.append("removedImages", JSON.stringify(removedImages))
      }

      await onSubmit(fd, post?.id)
      toast({
        title: "Success",
        description: isUpdateMode ? "Post updated successfully" : "Post created successfully",
      })
      onClose()
    } catch (error) {
      toast({
        title: "Error",
        description: "Failed to submit post",
        variant: "destructive",
      })
    } finally {
      setIsSubmitting(false)
    }
  }

  return (
    <ModalWrapper
      title={isUpdateMode ? "Update Post" : "Create Post"}
      description={
        isUpdateMode
          ? "Update post information"
          : "Fill in details to create a new post"
      }
      isOpen={isOpen}
      onClose={onClose}
    >
      <form onSubmit={handleSubmit} className="space-y-4">
        <div className="space-y-2">
          <Label htmlFor="title">Title</Label>
          <Input
            id="title"
            name="title"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            required
          />
        </div>

        <div className="space-y-2">
          <Label htmlFor="content">Content</Label>
          <Textarea
            id="content"
            name="content"
            rows={6}
            value={content}
            onChange={(e) => setContent(e.target.value)}
            required
          />
        </div>

        {/* <div className="space-y-2">
          <Label htmlFor="mainContent">Main Content (optional)</Label>
          <Textarea
            id="mainContent"
            name="mainContent"
            rows={3}
            value={mainContent}
            onChange={(e) => setMainContent(e.target.value)}
          />
        </div> */}

        {isUpdateMode && existingImages.length > 0 && (
          <div className="space-y-2">
            <h4 className="text-sm font-medium">Existing Images</h4>
            <div className="grid grid-cols-2 gap-2">
              {existingImages.map((imgUrl, i) => (
                <div key={i} className="relative aspect-video overflow-hidden rounded-md border">
                  <Image
                    src={imgUrl || "/placeholder.svg"}
                    alt={`Image ${i + 1}`}
                    width={450}
                    height={250}
                    className="h-full w-full object-cover"
                    onError={(e) => {
                      // Fallback to placeholder on error
                      e.currentTarget.src = "/placeholder.svg"
                    }}
                  />
                  <Button
                    variant="destructive"
                    size="sm"
                    className="absolute top-2 right-2"
                    onClick={() => handleRemoveExistingImage(i)}
                  >
                    Remove
                  </Button>
                </div>
              ))}
            </div>
          </div>
        )}
        {/* New Images to Upload */}
        {images.length > 0 && (
          <div className="space-y-2">
            <h4 className="text-sm font-medium">New Images to Upload</h4>
            <div className="grid grid-cols-2 gap-2">
              {images.map((file, index) => {
                const fileUrl = URL.createObjectURL(file)
                return (
                  <div key={index} className="relative aspect-video overflow-hidden rounded-md border">
                    <Image
                      src={fileUrl}
                      alt={file.name}
                      width={450}
                      height={250}
                      className="h-full w-full object-cover"
                    />
                    <Button
                      variant="destructive"
                      size="sm"
                      className="absolute top-2 right-2"
                      onClick={() => handleRemoveNewImage(index)}
                    >
                      Remove
                    </Button>
                  </div>
                )
              })}
            </div>
          </div>
        )}
        {/* Add Images */}
        <div className="space-y-2">
          <Label htmlFor="addImages">Add Images</Label>
          <Input
            type="file"
            id="addImages"
            name="addImages"
            multiple
            accept="image/*"
            onChange={handleAddImages}
          />
        </div>
        {/* Form Buttons */}
        <div className="flex justify-end space-x-2 pt-4">
          <Button type="button" variant="outline" onClick={onClose}>
            Cancel
          </Button>
          <Button type="submit" disabled={isSubmitting}>
            {isSubmitting
              ? (isUpdateMode ? "Updating..." : "Creating...")
              : (isUpdateMode ? "Update Post" : "Create Post")}
          </Button>
        </div>
      </form>
    </ModalWrapper>
  )
}
