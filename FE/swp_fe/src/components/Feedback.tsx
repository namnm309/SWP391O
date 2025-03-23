import { getfeedback } from "@/api/feedback";
import { FeedbackCard } from "@/components/FeedbackCard"
import type { Feedback } from "@/type/feedback"
import { useCallback, useEffect, useState } from "react";

export function Feedback() {
    const [feedbacks, setFeedbacks] = useState<Feedback[]>([]);
  
    const fetchFeedbacks = useCallback(async () => {
      try {
        const response = await getfeedback();
        setFeedbacks(response);
      } catch (error) {
        console.error(error);
      }
    }, []);
    
    useEffect(() => {
      fetchFeedbacks();
    }, [fetchFeedbacks]);

  return (
    <section className="w-full py-12 md:py-16">
      <div className="mx-auto max-w-2xl px-4 py-10 sm:px-6 lg:max-w-7xl lg:px-8">
        <h2 className="text-2xl font-bold tracking-tight mb-8">Phản hồi từ bệnh nhân</h2>
        <div className="grid gap-6 grid-cols-3">
          {feedbacks.slice(0,4).map((item) => (
            <FeedbackCard key={item.id} feedback={item} />
          ))}
        </div>
      </div>
    </section>
  )
}

