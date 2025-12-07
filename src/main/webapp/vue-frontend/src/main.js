import Vue from 'vue'
import App from './App.vue'
import router from './router'
import axios from 'axios'

import {
    Button,
    Cell,
    CellGroup,
    Dialog,
    Field,
    Icon,
    Image,
    Popup,
    Picker,
    DatetimePicker,
    Toast,
    Grid,
    GridItem,
    NavBar,
    Divider
} from 'vant';

Vue.config.productionTip = false

// 配置axios为全局可用
Vue.prototype.$axios = axios

Vue.use(Button);
Vue.use(Cell);
Vue.use(CellGroup);
Vue.use(Dialog);
Vue.use(Field);
Vue.use(Icon);
Vue.use(Image);
Vue.use(Popup);
Vue.use(Picker);
Vue.use(DatetimePicker);
Vue.use(Toast);
Vue.use(Grid);
Vue.use(GridItem);
Vue.use(NavBar);
Vue.use(Divider);

new Vue({
    el: '#app',
    router,
    components: { App },
    template: '<App/>'
});