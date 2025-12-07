<template>
    <div class="weui-msg">
        <div class="weui-msg__icon-area"><i class="weui-icon-warn weui-icon_msg"></i></div>
        <div class="weui-msg__text-area">
            <h2 class="weui-msg__title">易班登录失败</h2>
            <p class="weui-msg__desc" v-if="errorMessage" v-text="errorMessage"></p>
            <p class="weui-msg__desc" v-else>易班登录失败，请稍后重试</p>
        </div>
        <div class="weui-msg__opr-area">
            <p class="weui-btn-area">
                <a class="weui-btn weui-btn_primary" href="javascript:" @click="goBack">返回首页</a>
            </p>
        </div>
    </div>
</template>

<script>
export default {
    name: 'YiBanError',
    data() {
        return {
            errorMessage: ''
        };
    },
    mounted() {
        // 获取URL中的错误信息参数
        this.errorMessage = this.getQueryString('errorMessage');
        
        // 如果URL中没有错误信息，检查是否有ErrorMessage参数（可能是后端传递的）
        if (!this.errorMessage && this.$route.query.ErrorMessage) {
            this.errorMessage = this.$route.query.ErrorMessage;
        }
    },
    methods: {
        // 获取URL查询参数
        getQueryString(name) {
            const reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
            const r = window.location.search.substr(1).match(reg);
            if (r != null) {
                return decodeURIComponent(r[2]);
            }
            return null;
        },
        
        // 返回首页
        goBack() {
            window.location.href = '/index';
        }
    }
};
</script>

<style scoped>
@import '../../../static/css/common/common.css';
@import '../../../static/css/common/weui.min.css';
</style>