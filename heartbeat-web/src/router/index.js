import Vue from 'vue'
import Router from 'vue-router'
// import SignUp from '../components/Admin/SignUp'
// import Login from '../components/Admin/Login'
import Issues from '../components/Admin/Issues'
import IssueDetails from '../components/Admin/IssueDetails'
import AppDetails from '../components/Admin/AppDetails'
import Apps from '../components/Admin/Apps'
import Home from '../components/Home'
import AppServiceStack from '../components/App/AppServiceStack'
import { store } from '../store'

Vue.use(Router);


export default new Router({
  routes: [{
    path: '/',
    name: 'Home',
    component: Home
  },
  //   {
  //   path: '/signup',
  //   name: 'SignUp',
  //   component: SignUp
  // }, {
  //   path: '/login',
  //   name: 'Login',
  //   component: Login
  // },
    {
    path: '/admin/issues',
    name: 'Issues',
    component: Issues
  }, {
    path: '/admin/users/:id',
    name: 'IssueDetails',
    component: IssueDetails,
    props: true
  }, {
    path: '/admin/apps/new',
    name: 'AppDetails',
    component: AppDetails
  }, {
    path: '/admin/apps',
    name: 'Posts',
    component: Apps
  }, {
    path: '/:slug',
    name: 'AppServiceStack',
    component: AppServiceStack,
    props: true
  }]
})
