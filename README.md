# 전시대 신청 어플리케이션

> Spring boot, Spring JPA, Vue js, vuex, vue router, vue loader, webpack, mariaDB

## Build Setup

``` bash
# reserve 폴더(vue js webpack 프로젝트 폴더) 외부 라이브러리 설치(/reserve/reserve)
npm install

# dev 서버 동작 at localhost:8081 (spring boot는 8080포트로 설정되어있음, DB접근을 위해서 두 개의 서버를 돌려야함)
npm run dev

# spring boot에 개발한 Vue js 컴포넌트를 번들링
npm run build
```

For a detailed explanation on how things work, check out the [guide](http://vuejs-templates.github.io/webpack/) and [docs for vue-loader](http://vuejs.github.io/vue-loader).
