"use client"
import * as React from "react"
import { format, addMinutes, isSameDay } from "date-fns"
import { CalendarIcon, Clock } from "lucide-react"
import { cn } from "@/lib/utils"
import { Button } from "@/components/ui/button"
import { Calendar } from "@/components/ui/calendar"
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"

interface DateTimePickerProps {
  date: Date | undefined
  setDate: (date: Date | undefined) => void
  onClose?: () => void
  className?: string
}

export function DateTimePicker({ date, setDate, onClose, className }: DateTimePickerProps) {
  const [selectedDate, setSelectedDate] = React.useState<Date | undefined>(date)

  const today = React.useMemo(() => {
    const now = new Date()
    now.setHours(0, 0, 0, 0)
    return now
  }, [])

  const getMinTime = React.useCallback((date: Date | undefined) => {
    if (!date) return "07:30"

    const now = new Date()

    if (isSameDay(date, now)) {
      const minTime = addMinutes(now, 30)
      return format(minTime, "HH:mm")
    } else {
      return "07:30"
    }
  }, [])

  const getMaxTime = React.useCallback(() => {
    return "16:00"
  }, [])

  const timeOptions = React.useMemo(() => {
    if (!selectedDate) return []

    const options = []
    const minTimeStr = getMinTime(selectedDate)
    const maxTimeStr = getMaxTime()

    const [minHour, minMinute] = minTimeStr.split(":").map(Number)
    const [maxHour, maxMinute] = maxTimeStr.split(":").map(Number)

    let currentHour = minHour
    let currentMinute = Math.ceil(minMinute / 15) * 15

    if (currentMinute >= 60) {
      currentHour += 1
      currentMinute = 0
    }

    while (currentHour < maxHour || (currentHour === maxHour && currentMinute <= maxMinute)) {
      const hour = currentHour.toString().padStart(2, "0")
      const minute = currentMinute.toString().padStart(2, "0")
      options.push(`${hour}:${minute}`)

      currentMinute += 15
      if (currentMinute >= 60) {
        currentHour += 1
        currentMinute = 0
      }
    }

    return options
  }, [selectedDate, getMinTime, getMaxTime])

  const currentTimeValue = React.useMemo(() => {
    if (!selectedDate) return ""

    const timeStr = format(selectedDate, "HH:mm")

    if (timeOptions.includes(timeStr)) {
      return timeStr
    }

    return timeOptions.length > 0 ? timeOptions[0] : ""
  }, [selectedDate, timeOptions])

  const handleDateSelect = (newDate: Date | undefined) => {
    if (!newDate) {
      setSelectedDate(undefined)
      return
    }

    const minTimeStr = getMinTime(newDate)
    const [hours, minutes] = minTimeStr.split(":").map(Number)

    const dateWithTime = new Date(newDate)
    dateWithTime.setHours(hours, minutes, 0, 0)

    setSelectedDate(dateWithTime)
  }

  const handleTimeSelect = (time: string) => {
    if (!selectedDate) return

    const [hours, minutes] = time.split(":").map(Number)
    const newDate = new Date(selectedDate)
    newDate.setHours(hours, minutes, 0, 0)

    setSelectedDate(newDate)
  }

  const handleSave = () => {
    setDate(selectedDate)
    if (onClose) onClose()
  }

  return (
    <div className={cn("space-y-4", className)}>
      <div className="flex flex-col gap-4 sm:flex-row">
        <div className="flex-1">
          <label className="block text-sm font-medium mb-1">Date</label>
          <Popover>
            <PopoverTrigger asChild>
              <Button
                variant={"outline"}
                className={cn("w-full justify-start text-left font-normal", !selectedDate && "text-muted-foreground")}
              >
                <CalendarIcon className="mr-2 h-4 w-4" />
                {selectedDate ? format(selectedDate, "dd/MM/yyyy") : <span>Select date</span>}
              </Button>
            </PopoverTrigger>
            <PopoverContent className="w-auto p-0" align="start">
              <Calendar
                mode="single"
                selected={selectedDate}
                onSelect={handleDateSelect}
                disabled={(date) => date < today}
                initialFocus
              />
            </PopoverContent>
          </Popover>
        </div>

        <div className="flex-1">
          <label className="block text-sm font-medium mb-1">Time</label>
          <Select
            value={currentTimeValue}
            onValueChange={handleTimeSelect}
            disabled={!selectedDate || timeOptions.length === 0}
          >
            <SelectTrigger>
              <div className="flex items-center">
                <Clock className="mr-2 h-4 w-4" />
                <SelectValue placeholder="Select time" />
              </div>
            </SelectTrigger>
            <SelectContent>
              <div className="max-h-72 overflow-y-auto">
                {timeOptions.map((time) => (
                  <SelectItem key={time} value={time}>
                    {time}
                  </SelectItem>
                ))}
              </div>
            </SelectContent>
          </Select>
        </div>

        <div className="flex items-end gap-2">
          <Button variant="outline" onClick={onClose}>
            Cancel
          </Button>
          <Button onClick={handleSave} disabled={!selectedDate}>
            Save
          </Button>
        </div>
      </div>
    </div>
  )
}

