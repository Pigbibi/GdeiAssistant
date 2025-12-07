<template>
    <div class="yi-ban-authorize">
        <!-- 易班授权对话框将通过JavaScript动态加载 -->
    </div>
</template>

<script>
export default {
    name: 'YiBanAuthorize',
    mounted() {
        // 易班授权对话框初始化
        (function () {
            // 确保App对象存在
            if (typeof App !== 'undefined' && typeof App.AuthDialog !== 'undefined') {
                App.AuthDialog.show({
                    client_id: this.getQueryString('client_id'),
                    redirect_uri: this.getQueryString('redirect_uri')
                });
            }
        })();
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
        }
    }
};
</script>

<style scoped>
/* 这里可以添加组件样式 */
</style>