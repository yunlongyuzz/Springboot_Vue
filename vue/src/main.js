import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import Element from 'element-ui'
import axios from 'axios'

import mavonEditor from 'mavon-editor'

import "element-ui/lib/theme-chalk/index.css"
import 'mavon-editor/dist/css/index.css'

import "./axios"
import "./permission"

//使用elementUI组件
Vue.use(Element)
Vue.use(mavonEditor)

Vue.config.productionTip = false
//注册使用axios
Vue.prototype.$axios = axios

//固定不变的
new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
