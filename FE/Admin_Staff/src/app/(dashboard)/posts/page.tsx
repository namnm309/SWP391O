"use client"

import { useState, useEffect, useMemo, useCallback } from "react"
import type { ColumnDef } from "@tanstack/react-table"
import { Edit, Trash2, Eye, FileText, Plus, LayoutGrid, LayoutList } from "lucide-react"
import { DataTable } from "@/components/ui/data-table"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import axios from '@/utils/axiosConfig';
import { toast } from "@/hooks/use-toast"
import { Post } from "@/types/post"
import { PostCard } from "@/components/modals/post/PostCard"
import { ViewPostModal } from "@/components/modals/post/ViewPostModal"
import { CreateEditPostModal } from "@/components/modals/post/CreateEditViewModal"

export default function PostsPage() {

  const [posts, setPosts] = useState<Post[]>([])
  const [loading, setLoading] = useState(true)
  const [view, setView] = useState<"list" | "grid">("list")
  const [isViewModalOpen, setIsViewModalOpen] = useState(false)
  
  const [selectedPost, setSelectedPost] = useState<Post | null>(null)
  const [isModalOpen, setIsModalOpen] = useState(false)
  const [modalPost, setModalPost] = useState<Post | null>(null)

  const loadPosts = useCallback(async () => {
    try {
      setLoading(true)
      const response = await axios.get("/post/posts")
      const data: Post[] = response.data || []
      setPosts(data)
    } catch (error) {
      toast({
        title: "Error",
        description: "Failed to load posts",
        variant: "destructive",
      })
    } finally {
      setLoading(false)
    }
  }, [toast])

  useEffect(() => {
    loadPosts()
  }, [loadPosts])

  const handleCreate = () => {
    setModalPost(null)
    setIsModalOpen(true)
  }
  const handleEdit = (post: Post) => {
    setModalPost(post)
    setIsModalOpen(true)
  }
  const handleDelete = async (post: Post) => {
    await axios.delete(`/post/posts/${post.id}`,{
      headers: { "Content-Type": "multipart/form-data" },
    })
  }
  const handleView = (post: Post) => {
    setSelectedPost(post)
    setIsViewModalOpen(true)
  }

  const handleSubmitPost = async (formData: FormData, postId?: number) => {
    if (postId) {
      // update
      await axios.put(`/post/posts/${postId}`, formData, {
        headers: { "Content-Type": "multipart/form-data" },
      })
    } else {
      // create
      await axios.post(`/post/posts`, formData, {
        headers: { "Content-Type": "multipart/form-data" },
      })
    }
    await loadPosts()
  }

  const columns: ColumnDef<Post>[] = [
    {
      accessorKey: "id",
      header: "ID",
    },
    {
      accessorKey: "title",
      header: "Title",
    },
    {
      accessorKey: "authorName",
      header: "Author",
    },
    {
      accessorKey: "updatedAt",
      header: "Updated",
      cell: ({ row }) => {
        const date = row.getValue("updatedAt") as string
        return new Date(date).toLocaleDateString()
      },
    },
    {
      id: "actions",
      header: "Actions",
      cell: ({ row }) => {
        const post = row.original

        return (
          <div className="flex gap-2">
            <Button variant="outline" size="sm" onClick={() => handleView(post)}>
              <Eye className="h-4 w-4" />
            </Button>
            <Button variant="outline" size="sm" onClick={() => handleEdit(post)}>
              <Edit className="h-4 w-4" />
            </Button>
            <Button
              variant="outline"
              size="sm"
              onClick={() => handleDelete(post)}
              className="text-red-500 hover:text-red-700"
            >
              <Trash2 className="h-4 w-4" />
            </Button>
          </div>
        )
      },
    },
  ]

  return (
    <div className="space-y-4">
      <div className="flex items-center justify-between">
        <h1 className="text-2xl font-bold">Posts Management</h1>
        <Button onClick={handleCreate}>
          <Plus className="mr-2 h-4 w-4" />
          Create Post
        </Button>
      </div>

      <div className="grid gap-4 md:grid-cols-3">
        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium">Total Posts</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="flex items-center">
              <FileText className="mr-2 h-5 w-5 text-blue-600" />
              <div className="text-2xl font-bold">{posts.length}</div>
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium">Recent Posts (30 days)</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="flex items-center">
              <FileText className="mr-2 h-5 w-5 text-green-600" />
              <div className="text-2xl font-bold">
                {
                  posts.filter((post) => {
                    const date = new Date(post.createdAt)
                    const now = new Date()
                    const thirtyDaysAgo = new Date(now.getTime() - 30 * 24 * 60 * 60 * 1000)
                    return date >= thirtyDaysAgo
                  }).length
                }
              </div>
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium">Posts with Images</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="flex items-center">
              <FileText className="mr-2 h-5 w-5 text-purple-600" />
              <div className="text-2xl font-bold">{posts.filter((post) => post.imageList.length > 0).length}</div>
            </div>
          </CardContent>
        </Card>
      </div>

      <div className="flex items-center justify-between">
        <h2 className="text-lg font-medium">All Posts</h2>
        <div className="flex gap-2">
          <Button variant={view === "list" ? "default" : "outline"} size="sm" onClick={() => setView("list")}>
            <LayoutList className="mr-2 h-4 w-4" />
            List
          </Button>
          <Button variant={view === "grid" ? "default" : "outline"} size="sm" onClick={() => setView("grid")}>
            <LayoutGrid className="mr-2 h-4 w-4" />
            Grid
          </Button>
        </div>
      </div>

      {loading ? (
        <div className="flex h-40 items-center justify-center">
          <p>Loading posts...</p>
        </div>
      ) : (
        <>
          {view === "list" ? (
            <Card>
              <CardContent className="pt-6">
                <DataTable columns={columns} data={posts} searchColumn={["title", "id"]} searchPlaceholder="Search by title..." />
              </CardContent>
            </Card>
          ) : (
            <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
              {posts.map((post) => (
                <PostCard key={post.id} post={post} onView={() => handleView(post)} onEdit={() => handleEdit(post)} onDelete={() => handleDelete(post)} />
              ))}
            </div>
          )}
        </>
      )}

      {isViewModalOpen && selectedPost && (
        <ViewPostModal
          post={selectedPost}
          onClose={() => setIsViewModalOpen(false)}
        />
      )}

      {isModalOpen && (
        <CreateEditPostModal
          isOpen={isModalOpen}
          onClose={() => setIsModalOpen(false)}
          onSubmit={handleSubmitPost}
          post={modalPost}
        />
      )}
    </div>
  )
}

