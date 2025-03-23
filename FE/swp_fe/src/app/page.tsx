"use client";
import Products from "@/components/products";
import Autoplay from "embla-carousel-autoplay";
import { useCallback, useEffect, useRef, useState } from "react";
import Posts from "@/components/post";
import { Post } from "@/type/post";
import { getPosts } from "@/api/postApi";
import { PostCarousel } from "@/components/PostCarousel";
import { Incentives } from "@/components/Incentives";
import { Feedback } from "@/components/Feedback";

// const images = [
//   "https://vnvc.vn/wp-content/uploads/2024/10/banner-uu-dai-vac-xin-zona-than-kinh-mb.jpg",
//   "https://vnvc.vn/wp-content/uploads/2025/02/banner-vnvc-tang-cuong-gio-hoat-dong-mb.jpg",
//   "https://vnvc.vn/wp-content/uploads/2025/01/le-ky-ket-hop-tac-thiet-ke-nha-may-vac-xin-vnvc.jpg",
// ];

export default function Home() {

  const [posts, setPosts] = useState<Post[]>([]);

  const fetchPosts = useCallback(async () => {
    try {
      const response = await getPosts();
      setPosts(response);
    } catch (error) {
      console.error(error);
    }
  }, []);
  
  useEffect(() => {
    fetchPosts();
  }, [fetchPosts]);

  const plugin = useRef(Autoplay({ delay: 2000, stopOnInteraction: true }));
  return (
    <div className="flex-1 mt-4">
      {/* <div className="mx-auto px-4 sm:px-6 max-w-7xl lg:px-8 mt-4">
        <Carousel
          plugins={[plugin.current]}
          onMouseEnter={plugin.current.stop}
          onMouseLeave={plugin.current.reset}
          className="w-full"
        >
          <CarouselContent>
            {images.map((image, index) => (
              <CarouselItem key={index} className="basis ">
                <CardContent className="aspect-[16/5] relative">
                  <Image
                    src={image}
                    alt={`Slide ${index + 1}`}
                    fill
                    className="rounded-lg  "
                  />
                </CardContent>
              </CarouselItem>
            ))}
          </CarouselContent>
        </Carousel>
      </div> */}


      <PostCarousel posts={posts.slice(0,3)} />
      <Incentives />
      <Posts posts={posts.slice(0,6)} />
      <Products />
      <Feedback />
    </div>
  );
}
