<template>
    <div>
        <form @submit.prevent="handleSearch">
            <div class="input">
                <input type="text" v-model="searchForm.keywords" name="keywords" placeholder="搜索时请选择寻物或寻主" autofocus>
            </div>
            <div class="radios">
                <div class="radio">
                    <label class="radio1" @click="selectLost">
                        <input type="radio" name="type" value="1" v-model="searchForm.type" hidden><img :src="searchForm.type === '1' ? '/img/lostandfound/lost.png' : '/img/lostandfound/unlost.png'">寻物
                    </label>
                </div>
                <div class="radio">
                    <label class="radio2" @click="selectFound">
                        <input type="radio" name="type" value="0" v-model="searchForm.type" hidden><img :src="searchForm.type === '0' ? '/img/lostandfound/found.png' : '/img/lostandfound/unfound.png'">寻主
                    </label>
                </div>
            </div>
            <div class="buttons">
                <div class="button cancel">
                    <button type="button" @click="goBack">取消</button>
                </div>
                <div class="button ok">
                    <button type="submit">确定</button>
                </div>
            </div>
        </form>
    </div>
</template>

<script>
    import axios from 'axios'

export default {
    name: 'LostAndFoundSearch',
    data() {
        return {
            searchForm: {
                keywords: '',
                type: ''
            }
        }
    },
    mounted() {
    },
    methods: {
        //选择寻物
        selectLost() {
            this.searchForm.type = '1';
        },
        
        //选择寻主
        selectFound() {
            this.searchForm.type = '0';
        },
        
        //检查是否已选择寻物还是寻主
        checkLostTypeCheckedState() {
            if (!this.searchForm.type) {
                this.$dialog.alert({
                    title: '未选择查询类型',
                    message: '请选择寻物还是寻主'
                });
                return false;
            }
            else if (this.searchForm.keywords.length > 50 || this.searchForm.keywords.length <= 0) {
                this.$dialog.alert({
                    title: '关键词长度不合法',
                    message: '关键词限制为1到50字'
                });
                return false;
            }
            return true;
        },
        
        // 处理搜索提交
        handleSearch() {
            if (this.checkLostTypeCheckedState()) {
                // 跳转到搜索结果页面
                this.$router.push({
                    path: `/lostandfound/search/keyword/${encodeURIComponent(this.searchForm.keywords)}/type/${this.searchForm.type}/start/0`
                });
            }
        },
        
        // 返回上一页
        goBack() {
            this.$router.go(-1);
        }
    }
}
</script>

<style scoped>
    /* 保持原有CSS样式，这里可以添加特定的样式 */
</style>