"use client"

import { useState, useEffect } from "react"
import Link from "next/link"
import { Button } from "@/components/ui/button"
import type { Post } from "@/type/post"
import { useRouter } from "next/navigation";

interface PostCarouselProps {
  posts: Post[]
}

export function PostCarousel({ posts }: PostCarouselProps) {
  const router = useRouter();
  const [activeIndex, setActiveIndex] = useState(0)

  useEffect(() => {
    const interval = setInterval(() => {
      setActiveIndex((current) => (current + 1) % posts.length)
    }, 5000)
    return () => clearInterval(interval)
  }, [posts.length])

  return (
    <section className="w-full">
      <div className="container mx-auto">
        <div className="relative overflow-hidden rounded-lg">
          <div
            className="flex transition-transform duration-500 ease-in-out"
            style={{ transform: `translateX(-${activeIndex * 100}%)` }}
          >
            {posts.map((post) => (
              <div key={post.id} className="w-full flex-shrink-0" onClick={() => router.push(`/tin-tuc/${post.id}`)}>
                <div className="relative aspect-[21/9] overflow-hidden rounded-lg">
                  <img
                    src={post.imageList[0] || "/placeholder.svg?height=600&width=1200"}
                    alt={post.title}
                    className="object-cover w-full h-full"
                  />
                  <div className="absolute inset-0 bg-gradient-to-t from-black/70 to-transparent flex flex-col justify-end p-6">
                    <h2 className="text-2xl md:text-3xl lg:text-4xl font-bold text-white mb-2">{post.title}</h2>
                    <p className="text-white/80 mb-4 max-w-2xl hidden md:block">{post.content.substring(0, 120)}...</p>
                    {/* <Button asChild className="w-fit">
                      <Link href={`/tin-tuc/${post.id}`}>Đọc thêm</Link>
                    </Button> */}
                  </div>
                </div>
              </div>
            ))}
          </div>
          <div className="absolute bottom-4 left-1/2 flex -translate-x-1/2 space-x-2">
            {posts.map((_, index) => (
              <Button
                key={index}
                variant="outline"
                size="icon"
                className={`h-2 w-2 rounded-full p-0 ${index === activeIndex ? "bg-primary" : "bg-white/50"}`}
                onClick={() => setActiveIndex(index)}
              />
            ))}
          </div>
        </div>
      </div>
    </section>
  )
}

