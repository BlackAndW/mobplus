<template>
    <div class="page-header-index-wide">
        <a-card :bordered="true" style="min-height:500px">
            <div style="padding-top: 100px">
            <center>Welcome to MobPlus!</center>
            </div>
        </a-card>
    </div>
</template>

<script>
import { mixinDevice } from '@/utils/mixin';

export default {
    mixins: [mixinDevice],
    data () {
        return {

        };
    },
    computed: {

    },
    mounted () {

    },
    methods: {

    }
};
</script>

<style lang="less" scoped>

</style>
