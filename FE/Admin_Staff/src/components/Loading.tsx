import { useStore } from "@/store";
import React from "react";

const Loading: React.FC = () => {
  const isLoading = useStore((state) => state.loading.isLoading);

  return (
    <>
      {isLoading && (
        <div className="fixed inset-0 z-[9999999] flex items-center justify-center bg-black bg-opacity-50">
          <div className="animate-spin rounded-full h-12 w-12 border-4 border-t-4 border-[#FE8282] border-opacity-50 border-t-[#FE8282]"></div>
        </div>
      )}
    </>
  );
};

export default Loading;