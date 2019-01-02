import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import Vant from 'vant';
import 'vant/lib/index.css';


// import axios from 'axios'
// import VueAxios from 'vue-axios'
import {post,fetch,patch,put,upload} from './utils/Http'
Vue.config.productionTip = false

//定义全局变量
Vue.prototype.$post=post;
Vue.prototype.$fetch=fetch;
Vue.prototype.$upload=upload;
Vue.prototype.$patch=patch;
Vue.prototype.$put=put;
Vue.use(Vant);
// Vue.use(VueAxios, axios)

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
