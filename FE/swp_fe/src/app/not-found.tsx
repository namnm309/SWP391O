import Image from "next/image"

export default function Custom404() {
  return (<div className="h-screen flex justify-center items-center">
    <Image src={"/img/404-NotFound.png"} alt={"404 page not found"} fill/>
  </div>)
}