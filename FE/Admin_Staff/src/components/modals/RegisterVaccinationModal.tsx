"use client"
import React, { useEffect, useState, useMemo } from "react"
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogDescription,
} from "@/components/ui/dialog"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover"
import {
  Command,
  CommandEmpty,
  CommandGroup,
  CommandInput,
  CommandItem,
  CommandList,
} from "@/components/ui/command"
import { ChevronsUpDown, Trash2, Edit2 } from "lucide-react"
import { toast } from "@/hooks/use-toast"
import axios from "@/utils/axiosConfig"
import { useStore } from "@/store"
import { Patient } from "@/models/user"
import { format } from "date-fns"

import { DateTimePicker } from "../DateTimePicker"
import { Vaccine } from "@/types/vaccine"
import { Validate } from "@/utils/validate"

interface ChildSelection {
  childId: number
  childName: string
  vaccines: Vaccine[]
}

interface RegisterVaccinationModalProps {
  open: boolean
  onClose: () => void
}

export function RegisterVaccinationModal({ open, onClose }: RegisterVaccinationModalProps) {
  const { allUsers } = useStore((state) => state.profile)
  const allVaccines = useStore((state) => state.product.vaccines) as Vaccine[]

  const [selectedParentId, setSelectedParentId] = useState<number | "">("")
  const [selectedParent, setSelectedParent] = useState<Patient | null>(null)
  const [parentPopoverOpen, setParentPopoverOpen] = useState(false)

  const [firstName, setFirstName] = useState("")
  const [lastName, setLastName] = useState("")
  const [email, setEmail] = useState("")
  const [mobileNo, setMobileNo] = useState("")

  const [selectedList, setSelectedList] = useState<ChildSelection[]>([])

  const [tempChildId, setTempChildId] = useState<number | "">("")
  const [tempChildName, setTempChildName] = useState("")
  const [tempChildVaccines, setTempChildVaccines] = useState<Vaccine[]>([])
  const [childPopoverOpen, setChildPopoverOpen] = useState(false)
  const [vaccinePopoverOpen, setVaccinePopoverOpen] = useState(false)

  const [searchTerm, setSearchTerm] = useState("") // parent search
  const [childSearch, setChildSearch] = useState("")
  const [vaccineSearch, setVaccineSearch] = useState("")

  const [vaccinationDate, setVaccinationDate] = useState<Date | undefined>()

  const [showConfirmDialog, setShowConfirmDialog] = useState(false)

  const filteredUsers = useMemo(() => {
    return (
      allUsers?.filter(
        (user) =>
          user.id.toString().includes(searchTerm) || 
          user.username.toLowerCase().includes(searchTerm.toLowerCase()) ||
          user.fullname.toLowerCase().includes(searchTerm.toLowerCase())
      ) || []
    )
  }, [allUsers, searchTerm])

  const usedChildIds = selectedList.map((c) => c.childId)
  const childFiltered = useMemo(() => {
    if (!selectedParent) return []
    return (
      selectedParent.children?.filter((child) => {
        if (usedChildIds.includes(child.childId)) return false

        const matchName =
          child.fullname.toLowerCase().includes(childSearch.toLowerCase()) ||
          String(child.childId).includes(childSearch)

        return matchName
      }) || []
    )
  }, [selectedParent, childSearch, usedChildIds])

  const filteredVaccines = useMemo(() => {
    const results = allVaccines?.filter((v) => {
      if (tempChildVaccines.some((tv) => tv.id === v.id)) return false
      
      const matchName =
        v.title.toLowerCase().includes(vaccineSearch.toLowerCase()) ||
        String(v.id).includes(vaccineSearch)
      return matchName
    }) || []

    return results
  }, [allVaccines, vaccineSearch, tempChildVaccines])

  useEffect(() => {
    if (!selectedParentId) {
      setSelectedParent(null)
      setFirstName("")
      setLastName("")
      setEmail("")
      setMobileNo("")
      setSelectedList([])
      setTempChildId("")
      setTempChildName("")
      setTempChildVaccines([])
      return
    }

    const fetchUser = async () => {
      try {
        const token = localStorage.getItem("token")
        const response = await axios.get(`/user/${selectedParentId}`, {
          headers: { Authorization: `Bearer ${token}` },
        })

        const p: Patient = response.data
        setSelectedParent(p)

        const [fname, ...rest] = p.fullname.split(" ")
        setFirstName(fname || "")
        setLastName(rest.join(" ") || "")
        setEmail(p.email || "")
        setMobileNo(p.phone || "")

        setSelectedList([])
        setTempChildId("")
        setTempChildName("")
        setTempChildVaccines([])
      } catch (error) {
        console.error("Error fetching user details", error)
        toast({
          title: "Error",
          description: "Failed to load user details",
          variant: "destructive",
        })
      }
    }

    fetchUser()
  }, [selectedParentId, toast])

  const addVaccineToChild = (vaccine: Vaccine) => {
    setVaccinePopoverOpen(false)

    setTempChildVaccines((prev) => {
      if (prev.find((x) => x.id === vaccine.id)) {
        return prev 
      }
      return [...prev, vaccine]
    })
  }

  const removeVaccineFromChild = (vaccineId: number) => {
    setTempChildVaccines((prev) => prev.filter((v) => v.id !== vaccineId))
  }

  const handleConfirmChild = () => {
    if (!tempChildId) {
      toast({
        title: "Select a child",
        description: "Please pick a child first",
        variant: "destructive",
      })
      return
    }
    if (tempChildVaccines.length === 0) {
      toast({
        title: "No vaccines",
        description: "Please select at least one vaccine for this child",
        variant: "destructive",
      })
      return
    }

    const newEntry: ChildSelection = {
      childId: Number(tempChildId),
      childName: tempChildName,
      vaccines: [...tempChildVaccines],
    }

    setSelectedList((prev) => {
      const withoutThisChild = prev.filter((c) => c.childId !== newEntry.childId)
      return [...withoutThisChild, newEntry]
    })

    setTempChildId("")
    setTempChildName("")
    setTempChildVaccines([])
  }

  const handleEditChild = (childId: number) => {
    const target = selectedList.find((c) => c.childId === childId)
    if (!target) return

    setTempChildId(target.childId)
    setTempChildName(target.childName)
    setTempChildVaccines(target.vaccines)

    setSelectedList((prev) => prev.filter((c) => c.childId !== childId))
  }

  const handleRemoveChildSelection = (childId: number) => {
    setSelectedList((prev) => prev.filter((c) => c.childId !== childId))
  }

  const totalPrice = useMemo(() => {
    return selectedList.reduce((acc, child) => {
      const sumVaccines = child.vaccines.reduce((sum, v) => sum + v.price, 0)
      return acc + sumVaccines
    }, 0)
  }, [selectedList])

  const canCreate = selectedParent && vaccinationDate && selectedList.length > 0

  const handleOpenConfirm = () => {
    if (!canCreate) {
      toast({
        title: "Cannot create order",
        description: "Please select parent, date, and at least one child with vaccines",
        variant: "destructive",
      })
      return
    }
    setShowConfirmDialog(true)
  }

  const handleSubmit = async () => {
    if (!canCreate) return

    const childProductMap: { [childId: number]: number[] } = {}
    selectedList.forEach((c) => {
      childProductMap[c.childId] = c.vaccines.map((v) => v.id)
    })

    const vaccinationDateISO = format(vaccinationDate as Date, "yyyy-MM-dd'T'HH:mm:ss")
    const payload = {
      parentId: selectedParent?.id,
      firstName,
      lastName,
      email,
      mobileNo,
      childProductMap,
      vaccinationdate: vaccinationDateISO,
    }

    try {
      const token = localStorage.getItem("token")
      await axios.post("/order/staff/create-by-product", payload, {
        headers: { Authorization: `Bearer ${token}` },
        params: { parentId: selectedParent?.id },
      })

      toast({ title: "Success", description: "Order created successfully" })
      setShowConfirmDialog(false)
      onClose()
    } catch (err) {
      console.error(err)
      toast({
        title: "Error",
        description: "Failed to create order",
        variant: "destructive",
      })
    }
  }

  return (
    <Dialog open={open} onOpenChange={onClose}>
      <DialogContent className="max-h-[80svh] min-w-[60svw] overflow-y-auto">
        <DialogHeader>
          <DialogTitle>Register Vaccination for User</DialogTitle>
          <DialogDescription>
            Staff can help create an order for a selected user (parent).
          </DialogDescription>
        </DialogHeader>

        <div className="my-4 space-y-2">
          <Label>Select Parent</Label>
          <Popover open={parentPopoverOpen} onOpenChange={setParentPopoverOpen}>
            <PopoverTrigger asChild>
              <Button
                variant="outline"
                role="combobox"
                className="w-full justify-between"
                onClick={() => setParentPopoverOpen(true)}
              >
                {selectedParentId
                  ? allUsers.find((u) => u.id === selectedParentId)?.fullname
                  : "Select parent..."}
                <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
              </Button>
            </PopoverTrigger>
            <PopoverContent className="w-[calc(60svw-4rem)] p-0">
              <Command>
                <CommandInput
                  placeholder="Search parent..."
                  value={searchTerm}
                  onValueChange={setSearchTerm}
                />
                <CommandEmpty>No parent found.</CommandEmpty>
                <CommandGroup className="max-h-[300px] overflow-y-auto">
                  {filteredUsers.map((user) => (
                    <CommandItem
                      key={`${user.id} ${user.username} ${user.fullname} ${user.phone}`}
                      value={user.id.toString()}
                      onSelect={() => {
                        setSelectedParentId(user.id)
                        setParentPopoverOpen(false) // auto close
                      }}
                    >
                      {user.fullname} (ID: {user.id})
                    </CommandItem>
                  ))}
                </CommandGroup>
              </Command>
            </PopoverContent>
          </Popover>
        </div>

        {selectedParent && (
          <div className="mb-4 border rounded-md p-4 space-y-4">
            <Label className="mb-2">Guardian Info</Label>
            <div className="flex gap-4">
              <div className="w-1/2 space-y-2">
                <Label>First Name</Label>
                <Input value={firstName} onChange={(e) => setFirstName(e.target.value)} />
              </div>
              <div className="w-1/2 space-y-2">
                <Label>Last Name</Label>
                <Input value={lastName} onChange={(e) => setLastName(e.target.value)} />
              </div>
            </div>
            <div className="flex gap-4">
              <div className="w-1/2 space-y-2">
                <Label>Email</Label>
                <Input type="email" value={email} onChange={(e) => setEmail(e.target.value)} />
              </div>
              <div className="w-1/2 space-y-2">
                <Label>Mobile No</Label>
                <Input type="tel" value={mobileNo} onChange={(e) => setMobileNo(e.target.value)} />
              </div>
            </div>
          </div>
        )}

        {selectedParent && (
          <>
            <Label className="mb-2">Pick a Child & Vaccines</Label>

            <div className="border p-3 rounded-md space-y-4">
              <div>
                <Label>Choose Child</Label>
                <Popover open={childPopoverOpen} onOpenChange={setChildPopoverOpen}>
                  <PopoverTrigger asChild>
                    <Button
                      variant="outline"
                      role="combobox"
                      className="w-full justify-between"
                      onClick={() => setChildPopoverOpen(true)}
                    >
                      {tempChildId
                        ? selectedParent.children?.find((c) => c.childId === tempChildId)?.fullname
                        : "Select Child..."}
                      <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                    </Button>
                  </PopoverTrigger>
                  <PopoverContent className="w-[calc(60svw-4rem)] p-0">
                    <Command>
                      <CommandInput
                        placeholder="Search child..."
                        value={childSearch}
                        onValueChange={setChildSearch}
                      />
                      <CommandEmpty>No child found.</CommandEmpty>
                      <CommandGroup className="max-h-[200px] overflow-y-auto">
                        {childFiltered.map((child) => (
                          <CommandItem
                            key={child.childId}
                            value={child.childId.toString()}
                            onSelect={() => {
                              setTempChildId(child.childId)
                              setTempChildName(child.fullname)
                              setChildPopoverOpen(false)
                            }}
                          >
                            {child.fullname}
                          </CommandItem>
                        ))}
                      </CommandGroup>
                    </Command>
                  </PopoverContent>
                </Popover>
              </div>

              <div>
                <Label>Select Vaccine</Label>
                <Popover open={vaccinePopoverOpen} onOpenChange={setVaccinePopoverOpen}>
                  <PopoverTrigger asChild>
                    <Button
                      variant="outline"
                      role="combobox"
                      className="mt-1 w-full justify-between"
                      onClick={() => setVaccinePopoverOpen(true)}
                    >
                      Add a vaccine...
                      <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                    </Button>
                  </PopoverTrigger>
                  <PopoverContent className="w-[calc(60svw-4rem)] p-0 max-h-[250px] overflow-y-auto">
                    <Command>
                      <CommandInput
                        placeholder="Search vaccine..."
                        value={vaccineSearch}
                        onValueChange={setVaccineSearch}
                      />
                      <CommandList className="flex max-h-[250px] overflow-y-auto overscroll-contain">
                        <CommandEmpty>No vaccine found.</CommandEmpty>
                        <CommandGroup className="flex w-[calc(60svw-4rem)]">
                          {filteredVaccines.map((v) => (
                            <CommandItem
                              key={v.id}
                              value={`${v.title} ${v.id}`}
                              onSelect={() => addVaccineToChild(v)}
                              className="flex items-center justify-between"
                            >
                              <p>{v.title}</p>
                              {Validate.formatPrice(v.price)}
                            </CommandItem>
                          ))}
                        </CommandGroup>
                      </CommandList>
                    </Command>
                  </PopoverContent>
                </Popover>
              </div>

              {tempChildVaccines.length > 0 && (
                <div className="mt-2 space-y-2">
                  <p className="text-sm font-medium">Vaccines Selected:</p>
                  <div className="space-y-2">
                    {tempChildVaccines.map((v) => (
                      <div
                        key={v.id}
                        className="flex items-center justify-between rounded-md border p-2"
                      >
                        <span>
                          {v.title} - {Validate.formatPrice(v.price)}
                        </span>
                        <Button
                          variant="destructive"
                          size="sm"
                          onClick={() => removeVaccineFromChild(v.id)}
                        >
                          <Trash2 className="mr-1 h-4 w-4" />
                          Remove
                        </Button>
                      </div>
                    ))}
                  </div>
                </div>
              )}

              <div className="flex justify-end">
                <Button variant="outline" onClick={handleConfirmChild}>
                  Confirm This Child
                </Button>
              </div>
            </div>

            {selectedList.length > 0 && (
              <div className="mt-4 space-y-2">
                <Label>Children to Vaccinate</Label>
                {selectedList.map((entry) => (
                  <div
                    key={entry.childId}
                    className="flex flex-col gap-2 rounded-md border p-3 mb-2"
                  >
                    <div className="flex items-center justify-between">
                      <p className="font-medium">
                        Child: {entry.childName}
                      </p>
                      <div className="flex gap-2">
                        <Button
                          variant="outline"
                          size="sm"
                          onClick={() => handleEditChild(entry.childId)}
                        >
                          <Edit2 className="mr-1 h-4 w-4" /> Edit
                        </Button>
                        <Button
                          variant="destructive"
                          size="sm"
                          onClick={() => handleRemoveChildSelection(entry.childId)}
                        >
                          <Trash2 className="mr-1 h-4 w-4" /> Remove
                        </Button>
                      </div>
                    </div>
                    <ul className="pl-4 list-disc">
                      {entry.vaccines.map((v) => (
                        <li key={v.id} className="flex justify-between">
                          <p>{v.title}</p>
                          <p>{Validate.formatPrice(v.price)}</p>
                        </li>
                      ))}
                    </ul>
                  </div>
                ))}
              </div>
            )}
          </>
        )}

        <div className="my-4 space-y-2">
          <Label>Vaccination Date</Label>
          <DateTimePicker date={vaccinationDate} setDate={setVaccinationDate} showBtn={false} />
        </div>

        <div className="mt-6 flex justify-end gap-3">
          <Button variant="outline" onClick={onClose}>
            Cancel
          </Button>
          <Button
            onClick={handleOpenConfirm}
            disabled={!canCreate}
            className={!canCreate ? "cursor-not-allowed opacity-70" : ""}
          >
            Review & Submit
          </Button>
        </div>
      </DialogContent>

      <Dialog open={showConfirmDialog} onOpenChange={setShowConfirmDialog}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>Confirm Order</DialogTitle>
            <DialogDescription>
              Please review the children, vaccines, and total price before creating the order.
            </DialogDescription>
          </DialogHeader>
          <div className="space-y-4">
            <p className="font-medium">
              Total Price: {Validate.formatPrice(totalPrice)}
            </p>

            {selectedList.map((entry) => (
              <div key={entry.childId} className="border p-3 rounded-md space-y-2">
                <p className="font-semibold">
                  {entry.childName} (ID: {entry.childId})
                </p>
                <ul className="pl-4 list-disc">
                  {entry.vaccines.map((v) => (
                    <li key={v.id}>
                      {v.title} - {new Intl.NumberFormat("vn-VN", {
                        style: "currency",
                        currency: "vnd",
                      }).format(v.price)}
                    </li>
                  ))}
                </ul>
              </div>
            ))}

            <div className="flex justify-end gap-3">
              <Button variant="outline" onClick={() => setShowConfirmDialog(false)}>
                Cancel
              </Button>
              <Button onClick={handleSubmit}>Confirm & Create</Button>
            </div>
          </div>
        </DialogContent>
      </Dialog>
    </Dialog>
  )
}
