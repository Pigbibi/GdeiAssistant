<template>
  <div>
    <div class="weui-cells__title" @click="goBack">返回</div>
    <div class="hd">
      <h1 class="page_title">最近登录记录</h1>
    </div>
    <div class="recent-login-record">
      <div v-if="ipRecords.length > 0">
        <div class="weui-cells__title">最近一周的登录记录</div>
        <div class="weui-cells">
          <div class="weui-cell" v-for="(record, index) in ipRecords" :key="index">
            <div class="weui-cell__bd">
              <img src="/img/ipaddress/location.png" style="width: 20px; margin-right: 10px; display: block; float: left"/>
              <span>{{ getLocation(record) }}</span>
              <div class="weui-cell__desc" style="margin-top: 5px">
                <div class="weui-cell__bd">
                  <img src="/img/ipaddress/clock.png" style="width: 20px; margin-right: 10px; display: block; float: left"/>
                  <span>{{ formatTime(record.time) }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'IPAddress',
  data() {
    return {
      ipRecords: []
    }
  },
  mounted() {
    this.getIPAddressRecord()
  },
  methods: {
    goBack() {
      this.$router.go(-1)
    },
    getIPAddressRecord() {
      const loading = weui.loading('正在加载最近登录记录')
      axios.get('/api/ip/start/0/size/10')
        .then(response => {
          loading.hide()
          if (response.data.success) {
            this.ipRecords = response.data.data
          }
        })
        .catch(error => {
          loading.hide()
          console.error('获取登录记录失败:', error)
        })
    },
    getLocation(record) {
      let location = ''
      if (record.country != null && record.country !== '') {
        location += record.country
      }
      if (record.province != null && record.province !== '') {
        location += record.province
      }
      if (record.city != null && record.city !== '') {
        location += record.city
      }
      if (record.network != null && record.network !== '') {
        location += record.network
      }
      return location
    },
    formatTime(time) {
      const date = new Date(time)
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      const hour = String(date.getHours()).padStart(2, '0')
      const minute = String(date.getMinutes()).padStart(2, '0')
      const second = String(date.getSeconds()).padStart(2, '0')
      return `${year}-${month}-${day} ${hour}:${minute}:${second}`
    }
  }
}
</script>

<style scoped>
/* 样式继承自原项目，不需要额外修改 */
</style>