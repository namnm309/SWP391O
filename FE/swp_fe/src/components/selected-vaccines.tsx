import { Product } from "@/lib/product";

interface SelectedVaccinesProps {
  vaccines: Product[];
  onRemove: (index: number) => void;
}

export function SelectedVaccines({
  vaccines,
  onRemove,
}: SelectedVaccinesProps) {
  //   const total = vaccines.reduce((sum, vaccine) => sum + vaccine.price, 0);

  return (
    <div className="bg-white rounded-lg shadow-sm p-4">
      <div className="flex items-center gap-2 mb-4">
        <svg
          className="w-6 h-6 text-[#1a237e]"
          viewBox="0 0 24 24"
          fill="currentColor"
        >
          <path d="M19 3H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm-5 14H7v-2h7v2zm3-4H7v-2h10v2zm0-4H7V7h10v2z" />
        </svg>
        <h2 className="text-[#1a237e] font-bold text-lg">
          DANH SÁCH VẮC XIN CHỌN MUA
        </h2>
      </div>

      <div className="space-y-4">
        {vaccines.map((vaccine, index) => (
          <div key={index} className="border-b pb-4">
            <div className="flex justify-between items-start mb-2">
              <h3 className="text-[#1a237e] font-medium flex-1">
                {vaccine.title}
              </h3>
              <button
                onClick={() => onRemove(index)}
                className="text-gray-400 hover:text-gray-600"
              >
                ×
              </button>
            </div>
            <div className="text-sm text-gray-600">
              Tác dụng phụ: {vaccine.sideEffects}
            </div>
            <div className="text-sm text-gray-600">
              Nguồn gốc: {vaccine.manufacturer}
            </div>
            <div className="text-[#1a237e] font-medium mt-2">
              {new Intl.NumberFormat("vi-VN").format(vaccine.price)} VND
            </div>
          </div>
        ))}
      </div>

      {vaccines.length > 0 && (
        <button
          onClick={() => {
            const orderDetails = vaccines.map((vaccine) => (
              vaccine
            ));
            window.location.href = `/checkout?order=${encodeURIComponent(
              JSON.stringify(orderDetails)
            )}`;
          }}
          className="w-full bg-[#F5A623] text-black font-medium py-3 px-4 rounded mt-4 hover:bg-[#e69816] transition-colors"
        >
          ĐĂNG KÝ MŨI TIÊM
        </button>
      )}
    </div>
  );
}
