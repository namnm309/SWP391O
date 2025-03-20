"use client";
import Link from "next/link";
import { PostCard } from "./PostCard";
import { Post } from "@/type/post";
import { Button } from "./ui/button";
import { ChevronRightIcon } from "lucide-react";

interface PostCarouselProps {
  posts: Post[]
}

export default function Posts({ posts }: PostCarouselProps) {
  return (
    <div className="bg-white">
      <div className="mx-auto max-w-2xl px-4 py-10 sm:px-6 lg:max-w-7xl lg:px-8">
        <div className="container">
          <div className="flex items-center justify-between mb-8">
            <h2 className="text-2xl font-bold tracking-tight">Bài viết mới nhất</h2>
            <Button variant="ghost" className="gap-1" asChild>
              <Link href="/tin-tuc">
                Xem thêm <ChevronRightIcon className="h-4 w-4" />
              </Link>
            </Button>
          </div>
          <div className="grid gap-6 sm:grid-cols-2 lg:grid-cols-3">
            {posts.map((post) => (
              <PostCard key={post.id} post={post} />
            ))}
          </div>
        </div>

        {/* <div className="grid grid-cols-1 gap-y-4 sm:grid-cols-2 sm:gap-x-6 sm:gap-y-10 lg:grid-cols-3 lg:gap-x-8">
          {posts.map((product) => (
            <div
              key={product.id}
              className="group relative flex flex-col overflow-hidden rounded-lg border border-gray-200 bg-white"
            >
              <img
                alt={product.imageList[0]}
                src={product.imageList[0]}
                className="aspect-[3/4] w-full bg-gray-200 group-hover:opacity-75 sm:aspect-auto h-36"
              />
              <div className="flex flex-1 flex-col space-y-2 p-4">
                <h3 className="text-sm font-medium text-gray-900">
                  <Link href={`/post/${product.id}`}>
                    <span aria-hidden="true" className="absolute inset-0" />
                    {product.title}
                  </Link>
                </h3>
                <p className="text-sm text-gray-500 line-clamp-2">
                  {product.content}
                </p>
                <div className="flex flex-1 flex-col justify-end"></div>
              </div>
            </div>
          ))}
        </div> */}
      </div>
    </div>
  );
}
