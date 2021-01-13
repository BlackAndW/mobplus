<template>
    <a-icon
        :type="isFullscreen?'fullscreen-exit':'fullscreen'"
        @click="click"
    />
</template>

<script>
import screenfull from 'screenfull';

export default {
    data () {
        return {
            isFullscreen: false
        };
    },
    mounted () {
        this.init();
    },
    beforeDestroy () {
        this.destroy();
    },
    methods: {
        click: function () {
            if (!screenfull.enabled) {
                this.$message({
                    message: 'you browser can not work',
                    type: 'warning'
                });
                return false;
            }
            screenfull.toggle();
        },
        change: function () {
            this.isFullscreen = screenfull.isFullscreen;
        },
        init: function () {
            if (screenfull.enabled) {
                screenfull.on('change', this.change);
            }
        },
        destroy: function () {
            if (screenfull.enabled) {
                screenfull.off('change', this.change);
            }
        }
    }
};
</script>
