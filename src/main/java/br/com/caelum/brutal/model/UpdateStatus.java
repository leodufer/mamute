package br.com.caelum.brutal.model;

public enum UpdateStatus {
    
    NO_NEED_TO_APPROVE {
        @Override
        public int getHttpStatusCode() {
            return 200;
        }
    },
    PENDING {
        @Override
        public int getHttpStatusCode() {
            return 201;
        }
    },
    APPROVED {
        @Override
        public int getHttpStatusCode() {
            return 200;
        }
    },
    REFUSED {
        @Override
        public int getHttpStatusCode() {
            return 403;
        }
    };
    
    public abstract int getHttpStatusCode();
}