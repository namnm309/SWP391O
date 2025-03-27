"use client"
import { useState, useEffect } from "react"
import axios from "@/utils/axiosConfig"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Users, Syringe, DollarSign } from "lucide-react"
import { Tabs, TabsList, TabsTrigger } from "@/components/ui/tabs"
import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
} from "recharts"

export default function DashboardPage() {
  const [activePeriod, setActivePeriod] = useState<"7days" | "15days" | "30days">("7days")
  const [chartData, setChartData] = useState<any[]>([])
  const [topVaccines, setTopVaccines] = useState<any[]>([])

  useEffect(() => {
    const fetchChartData = async () => {
      try {
        const token = localStorage.getItem("token")
        const response = await axios.get("/dashboard/chart?days=30", {
          headers: {
            Authorization: `Bearer ${token}`
          }
        })
        const data = response.data
        if (data.code === 1000) {
          const sortedData = data.result.sort(
            (a: any, b: any) => new Date(a.date).getTime() - new Date(b.date).getTime()
          )
          setChartData(sortedData)
        }
      } catch (error) {
        console.error("Error fetching chart data:", error)
      }
    }
    fetchChartData()
  }, [])

  useEffect(() => {
    const fetchTopVaccines = async () => {
      try {
        const days = Number(activePeriod.replace("days", ""))
        const token = localStorage.getItem("token")
        const response = await axios.get(`/dashboard/top-5-vaccines?days=${days}`, {
          headers: {
            Authorization: `Bearer ${token}`
          }
        })
        const data = response.data
        if (data.code === 1000) {
          setTopVaccines(data.result)
        }
      } catch (error) {
        console.error("Error fetching top vaccines data:", error)
      }
    }
    fetchTopVaccines()
  }, [activePeriod])

  const daysToDisplay = Number(activePeriod.replace("days", ""))
  const displayChartData = chartData.slice(-daysToDisplay)

  const todayData = chartData.length > 0 ? chartData[chartData.length - 1] : null
  const stats = [
    {
      title: "New User Today",
      value: todayData ? todayData.newUser : "-",
      icon: Users,
      color: "text-blue-600",
    },
    {
      title: "Vaccinations Today",
      value: todayData ? todayData.countVaccine : "-",
      icon: Syringe,
      color: "text-green-600",
    },
    {
      title: "Revenue Today",
      value: todayData ? `$${todayData.revenueInDay}` : "-",
      icon: DollarSign,
      color: "text-purple-600",
    },
  ]

  const CustomTooltip = ({ active, payload, label }: any) => {
    if (active && payload && payload.length) {
      return (
        <div className="rounded border bg-white p-3 shadow-md">
          <p className="mb-2 font-medium">{label}</p>
          {payload.map((entry: any) => (
            <div key={entry.name} className="flex items-center gap-2 py-1">
              <div className="h-3 w-3 rounded-full" style={{ backgroundColor: entry.stroke }}></div>
              <p className="text-sm" style={{ color: entry.stroke }}>
                {entry.name}:{" "}
                {entry.name === "Revenue"
                  ? new Intl.NumberFormat("vn-VN", { style: "currency", currency: "VND" }).format(entry.value)
                  : entry.value}
              </p>
            </div>
          ))}
        </div>
      )
    }
    return null
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <h1 className="text-2xl font-bold text-gray-800">Dashboard</h1>
        <div className="text-sm text-gray-500">
          {new Date().toLocaleDateString("en-US", {
            weekday: "long",
            year: "numeric",
            month: "long",
            day: "numeric",
          })}
        </div>
      </div>

      <div className="grid gap-6 md:grid-cols-3">
        {stats.map((stat) => (
          <Card key={stat.title}>
            <CardHeader className="flex flex-row items-center justify-between pb-2">
              <CardTitle className="text-sm font-medium">{stat.title}</CardTitle>
              <stat.icon className={`h-5 w-5 ${stat.color}`} />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">{stat.value}</div>
            </CardContent>
          </Card>
        ))}
      </div>

      <div className="grid gap-6 grid-cols-4">
        <Card className="col-span-3">
          <CardHeader>
            <div className="flex items-center justify-between">
              <CardTitle>Performance Metrics</CardTitle>
              <Tabs
                defaultValue="7days"
                value={activePeriod}
                onValueChange={(v) => setActivePeriod(v as "7days" | "15days" | "30days")}
              >
                <TabsList>
                  <TabsTrigger value="7days">7 Days</TabsTrigger>
                  <TabsTrigger value="15days">15 Days</TabsTrigger>
                  <TabsTrigger value="30days">30 Days</TabsTrigger>
                </TabsList>
              </Tabs>
            </div>
          </CardHeader>
          <CardContent>
            <div className="h-[300px]">
              <ResponsiveContainer width="100%" height="100%">
                <LineChart
                  data={displayChartData}
                  margin={{ top: 5, right: 30, left: 20, bottom: 5 }}
                >
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="date" />
                  <YAxis yAxisId="left" orientation="left" stroke="#3b82f6" />
                  <YAxis yAxisId="right" orientation="right" stroke="#8b5cf6" tickFormatter={(value) =>
                      new Intl.NumberFormat("vn-VN", {
                        style: "currency",
                        currency: "VND",
                      }).format(value)
                    }/>
                  <Tooltip
                    content={<CustomTooltip />}
                    wrapperStyle={{ outline: "none" }}
                    cursor={{ strokeDasharray: "3 3" }}
                  />
                  <Legend />
                  <Line
                    yAxisId="left"
                    type="monotone"
                    dataKey="newUser"
                    stroke="#3b82f6"
                    strokeWidth={2}
                    activeDot={{ r: 6 }}
                    name="Users"
                  />
                  <Line
                    yAxisId="right"
                    type="monotone"
                    dataKey="revenueInDay"
                    stroke="#8b5cf6"
                    strokeWidth={2}
                    activeDot={{ r: 6 }}
                    name="Revenue"
                  />
                  <Line
                    yAxisId="left"
                    type="monotone"
                    dataKey="countVaccine"
                    stroke="#10b981"
                    strokeWidth={2}
                    activeDot={{ r: 6 }}
                    name="Vaccines"
                  />
                </LineChart>
              </ResponsiveContainer>
            </div>
          </CardContent>
        </Card>

        <Card className="col-span-1">
          <CardHeader>
            <CardTitle>Top Vaccines</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              {topVaccines.length > 0 ? (
                topVaccines.map((vaccine, i) => (
                  <div key={vaccine.name} className="flex items-center justify-between rounded-lg border p-3">
                    <div className="flex items-center gap-3">
                      <div className="flex h-8 w-8 items-center justify-center rounded-full bg-blue-100 text-blue-700">
                        {i + 1}
                      </div>
                      <span>{vaccine.name}</span>
                    </div>
                    <span className="font-medium">{vaccine.dose} doses</span>
                  </div>
                ))
              ) : (
                <p>No vaccine data available.</p>
              )}
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  )
}
