import { reactive } from 'vue'

const authStore = reactive({
  token: localStorage.getItem('token') || null,
  userID: localStorage.getItem('userID') || null,
  
  get isAuthenticated() {
    return !!this.token
  },
  
  login(token, userID) {
    this.token = token
    this.userID = userID
    localStorage.setItem('token', token)
    localStorage.setItem('userID', userID)
  },
  
  logout() {
    this.token = null
    this.userID = null
    localStorage.removeItem('token')
    localStorage.removeItem('userID')
  }
})

export function useAuthStore() {
  return authStore
}