package com.zyascend.NoBoring.bean;

/**
 * 功能：
 * 作者：zyascend on 2017/7/25 09:23
 * 邮箱：zyascend@qq.com
 */

public class FollowResponse {

    /**
     * user : {"__type":"Pointer","className":"_User","objectId":"5975d4101b69e6006c389592"}
     * follower : {"email":"zyascend@qq.com","updatedAt":"2017-07-24T11:21:22.264Z","objectId":"59754d1bfe88c2c1d45b156d","username":"Make It","createdAt":"2017-07-24T01:27:55.862Z","followerCount":1,"className":"_User","postCount":1,"emailVerified":true,"__type":"Pointer","avatar":{"__type":"Pointer","className":"_File","objectId":"59754d7f61ff4b006c717a4d"},"avatarUrl":"http://ac-ioJGwMTC.clouddn.com/9332772ad64e76e954d1.png","followeeCount":1,"mobilePhoneVerified":false}
     * createdAt : 2017-07-24T11:08:12.796Z
     * updatedAt : 2017-07-24T11:08:12.796Z
     * objectId : 5975d51c128fe155ce7a66a2
     */

    private UserBean user;
    private FollowBean follower;
    private String createdAt;
    private String updatedAt;
    private String objectId;

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public FollowBean getFollower() {
        return follower;
    }

    public void setFollower(FollowBean follower) {
        this.follower = follower;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public static class UserBean {
        /**
         * __type : Pointer
         * className : _User
         * objectId : 5975d4101b69e6006c389592
         */

        private String __type;
        private String className;
        private String objectId;

        public String get__type() {
            return __type;
        }

        public void set__type(String __type) {
            this.__type = __type;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getObjectId() {
            return objectId;
        }

        public void setObjectId(String objectId) {
            this.objectId = objectId;
        }
    }

    public static class FollowBean {
        /**
         * email : zyascend@qq.com
         * updatedAt : 2017-07-24T11:21:22.264Z
         * objectId : 59754d1bfe88c2c1d45b156d
         * username : Make It
         * createdAt : 2017-07-24T01:27:55.862Z
         * followerCount : 1
         * className : _User
         * postCount : 1
         * emailVerified : true
         * __type : Pointer
         * avatar : {"__type":"Pointer","className":"_File","objectId":"59754d7f61ff4b006c717a4d"}
         * avatarUrl : http://ac-ioJGwMTC.clouddn.com/9332772ad64e76e954d1.png
         * followeeCount : 1
         * mobilePhoneVerified : false
         */

        private String email;
        private String updatedAt;
        private String objectId;
        private String username;
        private String createdAt;
        private int followerCount;
        private String className;
        private int postCount;
        private boolean emailVerified;
        private String __type;
        private AvatarBean avatar;
        private String avatarUrl;
        private int followeeCount;
        private boolean mobilePhoneVerified;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getObjectId() {
            return objectId;
        }

        public void setObjectId(String objectId) {
            this.objectId = objectId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public int getFollowerCount() {
            return followerCount;
        }

        public void setFollowerCount(int followerCount) {
            this.followerCount = followerCount;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public int getPostCount() {
            return postCount;
        }

        public void setPostCount(int postCount) {
            this.postCount = postCount;
        }

        public boolean isEmailVerified() {
            return emailVerified;
        }

        public void setEmailVerified(boolean emailVerified) {
            this.emailVerified = emailVerified;
        }

        public String get__type() {
            return __type;
        }

        public void set__type(String __type) {
            this.__type = __type;
        }

        public AvatarBean getAvatar() {
            return avatar;
        }

        public void setAvatar(AvatarBean avatar) {
            this.avatar = avatar;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }

        public int getFolloweeCount() {
            return followeeCount;
        }

        public void setFolloweeCount(int followeeCount) {
            this.followeeCount = followeeCount;
        }

        public boolean isMobilePhoneVerified() {
            return mobilePhoneVerified;
        }

        public void setMobilePhoneVerified(boolean mobilePhoneVerified) {
            this.mobilePhoneVerified = mobilePhoneVerified;
        }

        public static class AvatarBean {
            /**
             * __type : Pointer
             * className : _File
             * objectId : 59754d7f61ff4b006c717a4d
             */

            private String __type;
            private String className;
            private String objectId;

            public String get__type() {
                return __type;
            }

            public void set__type(String __type) {
                this.__type = __type;
            }

            public String getClassName() {
                return className;
            }

            public void setClassName(String className) {
                this.className = className;
            }

            public String getObjectId() {
                return objectId;
            }

            public void setObjectId(String objectId) {
                this.objectId = objectId;
            }
        }
    }
}
