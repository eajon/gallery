import Vue from 'vue'
import Router from 'vue-router'
import Login from './views/Login'
import Folder from './views/Folder'
import Gallery from './views/Gallery'

Vue.use(Router)

export default new Router({
  base: process.env.BASE_URL,
  routes: [
    {
      path: '/',
      name: 'login',
      component: Login,
      meta:{
        title:"fsf"
      }
    },
    {
      path: '/gallery',
      name: 'gallery',
      component: Gallery
    },
    {
      path: '/folder',
      name: 'folder',
      component: Folder
    }
  ]
})
