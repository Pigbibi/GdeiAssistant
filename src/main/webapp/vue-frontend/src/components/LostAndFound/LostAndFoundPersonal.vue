<template>
    <div class="body1">
        <section class="head">
            <a href="javascript:history.go(-1);" class="back" @click="goBack"><i class="i iarr"></i></a>
            <span class="t2">我的失物招领</span>
        </section>

        <div class="personal">
            <div class="pic">
                <img :src="userAvatar" alt="">
            </div>
            <div class="info">
                <p class="name">{{ userName }}</p>
                <p class="motto">{{ userMotto }}</p>
            </div>
        </div>

        <div class="tab">
            <ul class="tabul">
                <li class="tabli" :class="{ active: activeTab === 'lost' }" @click="switchTab('lost')">
                    <a href="javascript:;" class="taba">我的失物</a>
                </li>
                <li class="tabli" :class="{ active: activeTab === 'found' }" @click="switchTab('found')">
                    <a href="javascript:;" class="taba">我的招领</a>
                </li>
                <li class="tabli" :class="{ active: activeTab === 'complete' }" @click="switchTab('complete')">
                    <a href="javascript:;" class="taba">已完成</a>
                </li>
            </ul>
        </div>

        <div class="content" v-if="activeTab === 'lost'">
            <div v-if="lostItems.length > 0" class="list">
                <ul>
                    <li v-for="(item, index) in lostItems" :key="index" class="li">
                        <a href="#" class="item" @click="goToDetail(item.lostFoundId)">
                            <div class="pic">
                                <i class="img">
                                    <img :src="getPreviewPicture(item.image, item.lostFoundId)" @load="checkimg" alt="">
                                </i>
                            </div>
                            <div class="txt">
                                <p class="t1">{{ item.name }}</p>
                                <p class="t2">
                                    <span>{{ item.itemType }}</span>
                                    <span>{{ item.location }}</span>
                                </p>
                                <p class="t3">{{ item.releaseTime }}</p>
                            </div>
                        </a>
                        <div class="opt">
                            <a href="#" class="o1" @click="editItem(item.lostFoundId)">编辑</a>
                            <a href="#" class="o2" @click="deleteItem(item.lostFoundId, index, 'lost')">删除</a>
                        </div>
                    </li>
                </ul>
            </div>
            <div v-else class="nodata">
                <p class="n1">暂无失物信息</p>
                <a href="#" class="add" @click="goToPublish">
                    <i class="i iadd"><i class="i i1"></i><i class="i i2"></i></i>
                    <p class="n2">发布我的失物</p>
                </a>
            </div>
        </div>

        <div class="content" v-if="activeTab === 'found'">
            <div v-if="foundItems.length > 0" class="list">
                <ul>
                    <li v-for="(item, index) in foundItems" :key="index" class="li">
                        <a href="#" class="item" @click="goToDetail(item.lostFoundId)">
                            <div class="pic">
                                <i class="img">
                                    <img :src="getPreviewPicture(item.image, item.lostFoundId)" @load="checkimg" alt="">
                                </i>
                            </div>
                            <div class="txt">
                                <p class="t1">{{ item.name }}</p>
                                <p class="t2">
                                    <span>{{ item.itemType }}</span>
                                    <span>{{ item.location }}</span>
                                </p>
                                <p class="t3">{{ item.releaseTime }}</p>
                            </div>
                        </a>
                        <div class="opt">
                            <a href="#" class="o1" @click="editItem(item.lostFoundId)">编辑</a>
                            <a href="#" class="o2" @click="deleteItem(item.lostFoundId, index, 'found')">删除</a>
                        </div>
                    </li>
                </ul>
            </div>
            <div v-else class="nodata">
                <p class="n1">暂无招领信息</p>
                <a href="#" class="add" @click="goToPublish">
                    <i class="i iadd"><i class="i i1"></i><i class="i i2"></i></i>
                    <p class="n2">发布我的招领</p>
                </a>
            </div>
        </div>

        <div class="content" v-if="activeTab === 'complete'">
            <div v-if="completedItems.length > 0" class="list">
                <ul>
                    <li v-for="(item, index) in completedItems" :key="index" class="li">
                        <a href="#" class="item" @click="goToDetail(item.lostFoundId)">
                            <div class="pic">
                                <i class="img">
                                    <img :src="getPreviewPicture(item.image, item.lostFoundId)" @load="checkimg" alt="">
                                </i>
                            </div>
                            <div class="txt">
                                <p class="t1">{{ item.name }}</p>
                                <p class="t2">
                                    <span>{{ item.itemType }}</span>
                                    <span>{{ item.location }}</span>
                                </p>
                                <p class="t3">{{ item.releaseTime }}</p>
                            </div>
                        </a>
                        <div class="opt">
                            <a href="#" class="o2" @click="deleteItem(item.lostFoundId, index, 'complete')">删除</a>
                        </div>
                    </li>
                </ul>
            </div>
            <div v-else class="nodata">
                <p class="n1">暂无已完成信息</p>
            </div>
        </div>
    </div>
</template>

<script>
    import axios from 'axios'

export default {
    name: 'LostAndFoundPersonal',
    data() {
        return {
            activeTab: 'lost', // 当前激活的标签页
            userAvatar: '/static/img/avatar.png', // 默认头像
            userName: '', // 用户名
            userMotto: '', // 用户个性签名
            lostItems: [], // 我的失物列表
            foundItems: [], // 我的招领列表
            completedItems: [], // 已完成列表
            loading: false // 加载状态
        }
    },
    mounted() {
        this.loadUserInfo();
        this.loadLostItems();
        this.loadFoundItems();
        this.loadCompletedItems();
    },
    methods: {
        // 返回上一页
        goBack() {
            this.$router.go(-1);
        },
        
        // 加载用户信息
        loadUserInfo() {
            axios.get('/api/user/info')
                .then(response => {
                    if (response.data.success) {
                        this.userName = response.data.data.userName;
                        this.userMotto = response.data.data.motto;
                        this.userAvatar = response.data.data.avatar || '/static/img/avatar.png';
                    }
                })
                .catch(error => {
                    console.error('获取用户信息失败:', error);
                });
        },
        
        // 切换标签页
        switchTab(tabName) {
            this.activeTab = tabName;
        },
        
        // 调整图片尺寸
        checkimg(e) {
            const obj = e.target;
            if (obj.width > obj.height) {
                obj.style.width = 'auto';
                obj.style.height = '100%';
            }
        },
        
        // 获取预览图片
        getPreviewPicture(image, itemId) {
            return `/api/lostandfound/item/id/${itemId}/preview?image=${encodeURIComponent(image)}`;
        },
        
        // 跳转到详情页
        goToDetail(itemId) {
            this.$router.push(`/lostandfound/item/${itemId}`);
        },
        
        // 跳转到编辑页
        editItem(itemId) {
            this.$router.push(`/lostandfound/edit/${itemId}`);
        },
        
        // 跳转到发布页
        goToPublish() {
            this.$router.push('/lostandfound/publish');
        },
        
        // 删除物品
        deleteItem(itemId, index, listType) {
            if (confirm('确定要删除该物品信息吗？')) {
                axios.delete(`/api/lostandfound/item/id/${itemId}`)
                    .then(response => {
                        if (response.data.success) {
                            // 根据列表类型删除对应的数据
                            if (listType === 'lost') {
                                this.lostItems.splice(index, 1);
                            } else if (listType === 'found') {
                                this.foundItems.splice(index, 1);
                            } else if (listType === 'complete') {
                                this.completedItems.splice(index, 1);
                            }
                        } else {
                            alert(response.data.message);
                        }
                    })
                    .catch(error => {
                        console.error('删除物品信息失败:', error);
                        alert('删除物品信息失败，请稍后重试');
                    });
            }
        },
        
        // 加载我的失物列表
        loadLostItems() {
            this.loading = true;
            axios.get('/api/lostandfound/user/lost')
                .then(response => {
                    if (response.data.success) {
                        this.lostItems = response.data.data;
                    }
                })
                .catch(error => {
                    console.error('获取我的失物列表失败:', error);
                })
                .finally(() => {
                    this.loading = false;
                });
        },
        
        // 加载我的招领列表
        loadFoundItems() {
            this.loading = true;
            axios.get('/api/lostandfound/user/found')
                .then(response => {
                    if (response.data.success) {
                        this.foundItems = response.data.data;
                    }
                })
                .catch(error => {
                    console.error('获取我的招领列表失败:', error);
                })
                .finally(() => {
                    this.loading = false;
                });
        },
        
        // 加载已完成列表
        loadCompletedItems() {
            this.loading = true;
            axios.get('/api/lostandfound/user/complete')
                .then(response => {
                    if (response.data.success) {
                        this.completedItems = response.data.data;
                    }
                })
                .catch(error => {
                    console.error('获取已完成列表失败:', error);
                })
                .finally(() => {
                    this.loading = false;
                });
        }
    }
}
</script>

<style scoped>
    /* 保持原有CSS样式，这里可以添加特定的样式 */
</style>