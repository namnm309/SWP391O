"use client"
import { useState } from "react"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Users, Syringe, DollarSign, Calendar } from "lucide-react"
import { Tabs, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from "recharts"


const generateMockData = (days: number) => {
  const data = []
  const now = new Date()

  for (let i = days - 1; i >= 0; i--) {
    const date = new Date(now)
    date.setDate(date.getDate() - i)

    // Random values for each metric
    const users = Math.floor(Math.random() * 15) + 5
    const income = Math.floor(Math.random() * 1500) + 500
    const vaccines = Math.floor(Math.random() * 40) + 10

    data.push({
      date: date.toLocaleDateString("en-US", { month: "short", day: "numeric" }),
      users,
      income,
      vaccines,
    })
  }

  return data
}

const chartData = {
  "7days": generateMockData(7),
  "15days": generateMockData(15),
  "30days": generateMockData(30),
}

export default function DashboardPage() {
  const [activePeriod, setActivePeriod] = useState<"7days" | "15days" | "30days">("7days")

  const stats = [
    {
      title: "Total Users",
      value: "1,234",
      icon: Users,
      change: "+12%",
      color: "text-blue-600",
    },
    {
      title: "Vaccinations Today",
      value: "42",
      icon: Syringe,
      change: "+5%",
      color: "text-green-600",
    },
    {
      title: "Revenue This Month",
      value: "$12,345",
      icon: DollarSign,
      change: "+8%",
      color: "text-purple-600",
    },
    {
      title: "Upcoming Appointments",
      value: "28",
      icon: Calendar,
      change: "+3%",
      color: "text-orange-600",
    },
  ]

  // Custom tooltip formatter for the chart
  const CustomTooltip = ({ active, payload, label }: any) => {
    if (active && payload && payload.length) {
      return (
        <div className="rounded border bg-white p-3 shadow-md">
          <p className="mb-2 font-medium">{label}</p>
          {payload.map((entry: any) => (
            <div key={entry.name} className="flex items-center gap-2 py-1">
              <div className="h-3 w-3 rounded-full" style={{ backgroundColor: entry.stroke }}></div>
              <p className="text-sm" style={{ color: entry.stroke }}>
                {entry.name}: {entry.name === "Revenue" ? "$" : ""}
                {entry.value}
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
        <h1 className="text-2xl font-bold text-gray-800">
          Dashboard
        </h1>
        <div className="text-sm text-gray-500">
          {new Date().toLocaleDateString("en-US", {
            weekday: "long",
            year: "numeric",
            month: "long",
            day: "numeric",
          })}
        </div>
      </div>

      <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-4">
        {stats.map((stat) => (
          <Card key={stat.title}>
            <CardHeader className="flex flex-row items-center justify-between pb-2">
              <CardTitle className="text-sm font-medium">{stat.title}</CardTitle>
              <stat.icon className={`h-5 w-5 ${stat.color}`} />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">{stat.value}</div>
              <p className="text-xs text-muted-foreground">
                <span className={`${stat.color}`}>{stat.change}</span> from last month
              </p>
            </CardContent>
          </Card>
        ))}
      </div>

      <div className="grid gap-6 grid-cols-4">
        <Card className="col-span-3">
          <CardHeader>
            <div className="flex items-center justify-between">
              <CardTitle>Performance Metrics</CardTitle>
              <Tabs defaultValue="7days" value={activePeriod} onValueChange={(v) => setActivePeriod(v as any)}>
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
                  data={chartData[activePeriod]}
                  margin={{
                    top: 5,
                    right: 30,
                    left: 20,
                    bottom: 5,
                  }}
                >
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="date" />
                  <YAxis yAxisId="left" orientation="left" stroke="#3b82f6" />
                  <YAxis yAxisId="right" orientation="right" stroke="#8b5cf6" />
                  <Tooltip
                    content={<CustomTooltip />}
                    wrapperStyle={{ outline: "none" }}
                    cursor={{ strokeDasharray: "3 3" }}
                  />
                  <Legend />
                  <Line
                    yAxisId="left"
                    type="monotone"
                    dataKey="users"
                    stroke="#3b82f6"
                    strokeWidth={2}
                    activeDot={{ r: 6 }}
                    name="Users"
                  />
                  <Line
                    yAxisId="right"
                    type="monotone"
                    dataKey="income"
                    stroke="#8b5cf6"
                    strokeWidth={2}
                    activeDot={{ r: 6 }}
                    name="Revenue"
                  />
                  <Line
                    yAxisId="left"
                    type="monotone"
                    dataKey="vaccines"
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
              {["Influenza", "COVID-19", "Hepatitis B", "Tetanus", "HPV"].map((vaccine, i) => (
                <div key={vaccine} className="flex items-center justify-between rounded-lg border p-3">
                  <div className="flex items-center gap-3">
                    <div className={`flex h-8 w-8 items-center justify-center rounded-full bg-blue-100 text-blue-700`}>
                      {i + 1}
                    </div>
                    <span>{vaccine}</span>
                  </div>
                  <span className="font-medium">{Math.floor(Math.random() * 100) + 50} doses</span>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  )
}

