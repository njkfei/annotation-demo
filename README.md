# annotation-demo
自定义注解处理器demo，基于aspectj搞

# 基本思路
* 先定义annotation，作用范围为RUNTIME
* 实现AOP,拦截@annotation，拿到@annotation上面的参数
* 在@annotation对应的AOP里面，搞业务处理
* ok

# 示例代码
annotation定义
```$xslt
@Target(ElementType.METHOD)
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheRedis {
    String key();
    int expireTime() default 60000;
}
```

你的业务代码
```$xslt
@Aspect
@Component
public class CacheService {
    Logger logger = LoggerFactory.getLogger(CacheService.class);

    @Pointcut(value = "@annotation(com.jpnie.demo.annotation.CacheRedis)")
    public void pointCut(){}

    @Before(value = "pointCut() && @annotation(cacheRedis)")
    public void before(CacheRedis cacheRedis) {
        logger.info("the result of this method will be cached.");
    }

    @AfterReturning(value = "pointCut() && @annotation(cacheRedis)",returning = "result")
    public void after(CacheRedis cacheRedis,Object result) {
        String key = cacheRedis.key();
        int expireTime = cacheRedis.expireTime();
        //do something...
        logger.info("-----redis-----[key = " + key + "]"+"[expireTime = " + expireTime + "]");
        logger.info("the result of this method is" + result + ",and has been cached.");
    }

    @Around("pointCut() && @annotation(cacheRedis)")
    public Object setCache(ProceedingJoinPoint joinPoint, CacheRedis cacheRedis) {
        Object result = 1;

        Method method = getMethod(joinPoint);//自定义注解类
        //CacheRedis cacheRedis = method.getAnnotation(CacheRedis.class);//获取key值
        String key = cacheRedis.key();
        int expireTime = cacheRedis.expireTime();
        //获取方法的返回类型,让缓存可以返回正确的类型
        Class returnType =((MethodSignature)joinPoint.getSignature()).getReturnType();

        logger.info("[key = " + key + "]"+"[expireTime = " + expireTime + "]");

        return result;
    }

    private Method getMethod(ProceedingJoinPoint joinPoint) {
        //获取参数的类型
        Method method = null;
        try {
            Signature signature = joinPoint.getSignature();
            MethodSignature msig = null;
            if (!(signature instanceof MethodSignature)) {
                throw new IllegalArgumentException("该注解只能用于方法");
            }
            msig = (MethodSignature) signature;
            method = joinPoint.getTarget().getClass().getMethod(msig.getName(), msig.getParameterTypes());
        } catch (NoSuchMethodException e) {
            logger.error("annotation no sucheMehtod", e);
        } catch (SecurityException e) {
            logger.error("annotation SecurityException", e);
        }
        return method;
    }
}

```

demo
```$xslt
@Service
public class DemoService {
    @CacheRedis(key = "test1",expireTime = 100)
    public int test1(int i){
        return i;
    }

    @CacheRedis(key="test2")
    public String test2(String str){
        return str;
    }
}
```