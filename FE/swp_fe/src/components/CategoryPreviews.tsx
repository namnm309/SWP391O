import Image from "next/image";

export default function CategoryPreviews() {
  return (
    <div className=" ">
      <div className="mx-auto   px-4   sm:px-6  max-w-7xl lg:px-8">
        <div className="mt-6 grid grid-cols-1 gap-y-6 sm:grid-cols-2 sm:grid-rows-2 sm:gap-x-6 lg:gap-8">
          <div className="group relative aspect-[2/1] overflow-hidden rounded-lg sm:row-span-2 sm:aspect-square">
            <Image
              alt="Two models wearing women's black cotton crewneck tee and off-white cotton crewneck tee."
              src="/img/a1.png"
              width={500}
              height={500}
              className="absolute size-full object-cover group-hover:opacity-75"
            />
            <div
              aria-hidden="true"
              className="absolute inset-0 bg-gradient-to-b from-transparent to-black opacity-50"
            />
            <div className="absolute inset-0 flex items-end p-6">
              <div>
                <h3 className="font-semibold text-white">
                  <a href="#">
                    <span className="absolute inset-0" />
                    Vaccine
                  </a>
                </h3>
                <p aria-hidden="true" className="mt-1 text-sm text-white">
                  Shop now
                </p>
              </div>
            </div>
          </div>
          <div className="group relative aspect-[2/1] overflow-hidden rounded-lg sm:aspect-auto">
            <Image
              alt="Two models wearing women's black cotton crewneck tee and off-white cotton crewneck tee."
              src="/img/a2.png"
              width={500}
              height={500}
              className="absolute size-full object-cover group-hover:opacity-75"
            />
            <div
              aria-hidden="true"
              className="absolute inset-0 bg-gradient-to-b from-transparent to-black opacity-50"
            />
            <div className="absolute inset-0 flex items-end p-6">
              <div>
                <h3 className="font-semibold text-white">
                  <a href="#">
                    <span className="absolute inset-0" />
                    Vaccine cho bé
                  </a>
                </h3>
                <p aria-hidden="true" className="mt-1 text-sm text-white">
                  Xem ngay
                </p>
              </div>
            </div>
          </div>
          <div className="group relative aspect-[2/1] overflow-hidden rounded-lg sm:aspect-auto">
            <Image
              alt="Two models wearing women's black cotton crewneck tee and off-white cotton crewneck tee."
              src="/img/a3.jpg"
              width={500}
              height={500}
              className="absolute size-full object-cover group-hover:opacity-75"
            />
            <div
              aria-hidden="true"
              className="absolute inset-0 bg-gradient-to-b from-transparent to-black opacity-50"
            />
            <div className="absolute inset-0 flex items-end p-6">
              <div>
                <h3 className="font-semibold text-white">
                  <a href="#">
                    <span className="absolute inset-0" />
                    Vaccine cho trẻ
                  </a>
                </h3>
                <p aria-hidden="true" className="mt-1 text-sm text-white">
                  Shop now
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
