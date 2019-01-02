import axios from 'axios';
import store from '@/store'
import Qs from 'qs'

axios.defaults.timeout = 5000;
axios.defaults.baseURL = "/api";
axios.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest'

//http request 拦截器
axios.interceptors.request.use(
  config => {
    if (/(patch)|(post)|(put)/i.test(config.method) && config.data && config.data.constructor !== FormData) {
      config.data = Qs.stringify(config.data);
    }
    // const token = getCookie('名称');注意使用的时候需要引入cookie方法，推荐js-cookie
    config.headers = {
      'Content-Type': 'application/x-www-form-urlencoded',
      'Access-Control-Allow-Origin': '*'
    }

    if (store.state.token) {
      // console.log(`Bearer ${store.state.token}`)
      config.headers.Authorization = `Bearer ${store.state.token}`
    }

    // if(token){
    //   config.params = {'token':token}
    // }
    return config;
  }
  //   ,
  //   error => {
  //     return Promise.reject(err);
  //   }
);


// //http response 拦截器
// axios.interceptors.response.use(
//   response => {
//     if(response.data.errCode ==2){
//       router.push({
//         path:"/login",
//         querry:{redirect:router.currentRoute.fullPath}//从哪个页面跳转
//       })
//     }
//     return response;
//   },
//   error => {
//     return Promise.reject(error)
//   }
// )


/**
 * 封装get方法
 * @param url
 * @param data
 * @returns {Promise}
 */

export function fetch(url, params = {}) {
  return new Promise((resolve, reject) => {
    axios.get(url, {
      params: params
    })
      .then(response => {
        resolve(response.data);
      })
      .catch(err => {
        reject(err)
      })
  })
}

/**
 * 封装delete方法
 * @param url
 * @param data
 * @returns {Promise}
 */
export function del(url, params = {}) {
  return new Promise((resolve, reject) => {
    axios.delete(url, {
      params: params
    })
      .then(response => {
        resolve(response.data);
      })
      .catch(err => {
        reject(err)
      })
  })
}



/**
 * 封装post请求
 * @param url
 * @param data
 * @returns {Promise}
 */

export function post(url, data = {}) {
  return new Promise((resolve, reject) => {
    axios.post(url, data)
      .then(response => {
        resolve(response.data);
      }, err => {
        reject(err)
      })
  })
}

/**
 * 封装upload请求
 * @param url
 * @param data
 * @returns {Promise}
 */

export function upload(url, data = {}) {
  let config = {
    headers: { 'Content-Type': 'multipart/form-data' }
  };
  return new Promise((resolve, reject) => {
    axios.post(url, data, config)
      .then(response => {
        resolve(response.data);
      }, err => {
        reject(err)
      })
  })
}

/**
* 封装patch请求
* @param url
* @param data
* @returns {Promise}
*/

export function patch(url, data = {}) {
  return new Promise((resolve, reject) => {
    axios.patch(url, data)
      .then(response => {
        resolve(response.data);
      }, err => {
        reject(err)
      })
  })
}

/**
* 封装put请求
* @param url
* @param data
* @returns {Promise}
*/

export function put(url, data = {}) {
  return new Promise((resolve, reject) => {
    axios.put(url, data)
      .then(response => {
        resolve(response.data);
      }, err => {
        reject(err)
      })
  })
}