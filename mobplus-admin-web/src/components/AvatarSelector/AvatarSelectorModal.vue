<template>
    <a-modal :visible="visible" @ok="onSubmit" @cancel="onCancel">
        <avatar-selector :value="currentSelectedIcon" @change="handleIconChange" />
    </a-modal>
</template>

<script>
import AvatarSelector from './AvatarSelector';

export default {
    components: {
        AvatarSelector
    },
    data () {
        return {
            visible: false,

            // currentSelectedIcon: 'pause-circle'
            currentSelectedIcon: 'https://randomuser.me/api/portraits/women/68.jpg'
        };
    },
    methods: {
        show: function (avatar) {
            this.currentSelectedIcon = avatar;
            this.visible = true;
        },
        close: function (success) {
            this.visible = false;
        },
        onCancel: function () {
            this.close(false);
        },
        onSubmit: function () {
            this.$emit('change', this.currentSelectedIcon);
            this.close(false);
        },
        handleIconChange (icon) {
            this.currentSelectedIcon = icon;
            this.onSubmit();
        }
    }
};
</script>
