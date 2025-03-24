import { useEffect } from 'react'
import { useStore } from '@/store'

export const useRole = () => {
  const roles = useStore(state => state.management.roles)
  const permissions = useStore(state => state.management.permissions)
  const fetchRoles = useStore(state => state.fetchRoles)
  const fetchPermissions = useStore(state => state.fetchPermissions)

  useEffect(() => {
    fetchRoles()
    fetchPermissions()
  }, [fetchRoles, fetchPermissions])

  return { roles, permissions }
}