"use client"
import { useState } from "react"
import { AlertCircle, Calendar, Clock, Package, Syringe, Users } from "lucide-react"
import { Badge } from "@/components/ui/badge"
import { Button } from "@/components/ui/button"
import { Separator } from "@/components/ui/separator"
import { PreviewCard } from "@/components/ui/preview-card"
import { Vaccine } from "@/types/vaccine"

interface VaccinePreviewProps {
  vaccine: Vaccine
  onClose: () => void
}

export function VaccinePreview({ vaccine, onClose }: VaccinePreviewProps) {
  const [showFullDescription, setShowFullDescription] = useState(false)
  const [showFullSideEffects, setShowFullSideEffects] = useState(false)
  const [showFullSchedule, setShowFullSchedule] = useState(false)

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString("en-US", {
      year: "numeric",
      month: "long",
      day: "numeric",
    })
  }

  const formatPrice = (price: number) => {
    return new Intl.NumberFormat("en-US", {
      style: "currency",
      currency: "USD",
    }).format(price)
  }

  const vaccineName = vaccine.title
  const vaccineId = vaccine.id

  return (
    <PreviewCard title={vaccine.title} onClose={onClose}>
      <div className="space-y-4">
        <div className="flex items-center justify-between">
          <Badge variant="outline" className="px-3 py-1">
            ID: {vaccineId}
          </Badge>
          <div className="flex gap-2">
            {vaccine.discount && vaccine.discount > 0 ? (
              <>
                <Badge variant="outline" className="line-through text-gray-500">
                  {formatPrice(vaccine.price)}
                </Badge>
                <Badge className="bg-green-100 text-green-800 hover:bg-green-100">
                  {formatPrice(vaccine.discountPrice || vaccine.price - (vaccine.price * vaccine.discount) / 100)}
                </Badge>
              </>
            ) : (
              <Badge className="bg-blue-100 text-blue-800 hover:bg-blue-100">{formatPrice(vaccine.price)}</Badge>
            )}
          </div>
        </div>

        {vaccine.description && (
          <div className="space-y-1">
            <h3 className="text-sm font-medium text-gray-500">Description</h3>
            <p className="text-sm">
              {showFullDescription || vaccine.description.length <= 150
                ? vaccine.description
                : `${vaccine.description.substring(0, 150)}...`}
              {vaccine.description.length > 150 && (
                <Button
                  variant="link"
                  className="h-auto p-0 text-blue-600"
                  onClick={() => setShowFullDescription(!showFullDescription)}
                >
                  {showFullDescription ? "Show less" : "Show more"}
                </Button>
              )}
            </p>
          </div>
        )}

        <Separator />

        {vaccine.manufacturer && (
          <div className="space-y-1">
            <h3 className="flex items-center gap-1 text-sm font-medium text-gray-500">
              <Package className="h-4 w-4" /> Manufacturer
            </h3>
            <p className="text-sm">{vaccine.manufacturer}</p>
          </div>
        )}

        {vaccine.targetGroup && (
          <div className="space-y-1">
            <h3 className="flex items-center gap-1 text-sm font-medium text-gray-500">
              <Users className="h-4 w-4" /> Target Group
            </h3>
            <p className="text-sm">{vaccine.targetGroup}</p>
          </div>
        )}

        {vaccine.numberOfDoses && (
          <div className="space-y-1">
            <h3 className="text-sm font-medium text-gray-500">Number of Doses</h3>
            <p className="text-sm">{vaccine.numberOfDoses}</p>
          </div>
        )}

        {vaccine.schedule && (
          <div className="space-y-1">
            <h3 className="text-sm font-medium text-gray-500">Schedule</h3>
            <p className="text-sm">
              {showFullSchedule || vaccine.schedule.length <= 100
                ? vaccine.schedule
                : `${vaccine.schedule.substring(0, 100)}...`}
              {vaccine.schedule.length > 100 && (
                <Button
                  variant="link"
                  className="h-auto p-0 text-blue-600"
                  onClick={() => setShowFullSchedule(!showFullSchedule)}
                >
                  {showFullSchedule ? "Show less" : "Show more"}
                </Button>
              )}
            </p>
          </div>
        )}

        {vaccine.sideEffects && (
          <div className="space-y-1">
            <h3 className="flex items-center gap-1 text-sm font-medium text-gray-500">
              <AlertCircle className="h-4 w-4" /> Side Effects
            </h3>
            <p className="text-sm">
              {showFullSideEffects || vaccine.sideEffects.length <= 100
                ? vaccine.sideEffects
                : `${vaccine.sideEffects.substring(0, 100)}...`}
              {vaccine.sideEffects.length > 100 && (
                <Button
                  variant="link"
                  className="h-auto p-0 text-blue-600"
                  onClick={() => setShowFullSideEffects(!showFullSideEffects)}
                >
                  {showFullSideEffects ? "Show less" : "Show more"}
                </Button>
              )}
            </p>
          </div>
        )}
      </div>
    </PreviewCard>
  )
}
