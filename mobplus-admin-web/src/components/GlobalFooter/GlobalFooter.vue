<template>
    <div class="footer">
        <div class="links"></div>
        <div class="copyright">
            Copyright
            <a-icon type="copyright" />
            2018-2020
            <span>YeeCloud Technology Co.ltd. All Right Reserved</span>
        </div>
    </div>
</template>

<script>
export default {
    name: 'GlobalFooter',
    data () {
        return {};
    }
};
</script>

<style lang="less" scoped>
.footer {
    padding: 0 16px;
    margin: 38px 0 15px;
    text-align: center;

    .links {
        margin-bottom: 8px;

        a {
            color: rgba(0, 0, 0, 0.45);

            &:hover {
                color: rgba(0, 0, 0, 0.65);
            }

            &:not(:last-child) {
                margin-right: 40px;
            }
        }
    }
    .copyright {
        color: rgba(0, 0, 0, 0.45);
        font-size: 14px;
    }
}
</style>
