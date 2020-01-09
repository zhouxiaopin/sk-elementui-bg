package cn.sk.temp.sys.shiro;

import cn.sk.temp.sys.common.SysConst;
import cn.sk.temp.sys.common.TokenCache;
import cn.sk.temp.sys.mapper.SysPermisMapper;
import cn.sk.temp.sys.mapper.SysRoleMapper;
import cn.sk.temp.sys.pojo.SysUserCustom;
import cn.sk.temp.sys.pojo.SysUserQueryVo;
import cn.sk.temp.sys.service.ISysUserService;
import cn.sk.temp.sys.utils.JwtUtil;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public class SkShiroRealm extends AuthorizingRealm {

    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysPermisMapper sysPermisMapper;


    /**
     * 必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 用户信息认证是在用户进行登录的时候进行验证(不存redis)
     * 也就是说验证用户输入的账号和密码是否正确，错误抛出异常
     *
     * @param auth 用户登录的账号密码信息
     * @return 返回封装了用户信息的 AuthenticationInfo 实例
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken auth) throws AuthenticationException {
        log.info("***********身份认证************");

        String token = (String) auth.getCredentials();
        if (token == null) {
            throw new AuthenticationException("token为空!");
        }
        // 解密获得username，用于和数据库进行对比
        String username = JwtUtil.getUsername(token);
        if (username == null) {
            throw new AuthenticationException("token非法无效!");
        }


        //1. 把 AuthenticationToken 转换为 UsernamePasswordToken
//        UsernamePasswordToken upToken = (UsernamePasswordToken) auth;

        //2. 从 UsernamePasswordToken 中来获取 username
//        String username = upToken.getUsername();

        SysUserQueryVo sysUserQueryVo = new SysUserQueryVo();

        SysUserCustom condition = new SysUserCustom();
        condition.setUserName(username);
        sysUserQueryVo.getIsNoLike().put("userName",true);
        sysUserQueryVo.setSysUserCustom(condition);


        List<SysUserCustom> sysUserCustoms = sysUserService.queryObjs(sysUserQueryVo).getData();

        //3. 调用数据库的方法, 从数据库中查询 username 对应的用户记录
        log.info("从数据库中获取 username: " + username + " 所对应的用户信息.");


        //4. 若用户不存在, 则可以抛出 UnknownAccountException 异常
        if(CollectionUtils.isEmpty(sysUserCustoms)) {
            throw new UnknownAccountException("用户不存在!");
        }

        SysUserCustom sysUserCustom = sysUserCustoms.get(0);

        //5. 根据用户信息的情况, 决定是否需要抛出其他的 AuthenticationException 异常.


        //6. 根据用户的情况, 来构建 AuthenticationInfo 对象并返回. 通常使用的实现类为: SimpleAuthenticationInfo
        //以下信息是从数据库中获取的.
        //1). principal: 认证的实体信息. 可以是 username, 也可以是数据表对应的用户的实体类对象.
        Object principal = sysUserCustom;
        //2). credentials: 密码.
        Object credentials = sysUserCustom.getPassword(); //"fc1709d0a95a6be30bc5926fdb7f22f4";
        String salt = sysUserCustom.getSalt();
//        if(!credentials.equals(ShiroUtils.getMd5Pwd(salt,new String(upToken.getPassword())))) {
//            throw new IncorrectCredentialsException("密码错误");
//        }

        if(StringUtils.equals(SysConst.RecordStatus.DISABLE,sysUserCustom.getRecordStatus())) {
            throw new LockedAccountException("用户被禁用");
        }

        // 校验token是否超时失效 & 或者账号密码是否错误
        if (!jwtTokenRefresh(token, username, sysUserCustom.getPassword())) {
//            throw new AuthenticationException("Token失效，请重新登录!");
            throw new AuthenticationException("Token失效，请重新登录!");
//            throw new AuthenticationException("Token失效，请重新登录!");
        }
//        if("admin".equals(username)){
//            credentials = "038bdaf98f2037b31f1e75b5b4c9b26e";
//        }

        //3). realmName: 当前 realm 对象的 name. 调用父类的 getName() 方法即可
        String realmName = getName();
        //4). 盐值.
//        ByteSource credentialsSalt = ByteSource.Util.bytes(username);
        ByteSource credentialsSalt = ByteSource.Util.bytes(salt);

        SimpleAuthenticationInfo info = null; //new SimpleAuthenticationInfo(principal, credentials, realmName);
        info = new SimpleAuthenticationInfo(principal, token, realmName);
//        info = new SimpleAuthenticationInfo(principal, credentials, credentialsSalt, realmName);
//        info = new SimpleAuthenticationInfo(principal, credentials, credentialsSalt, realmName);
        return info;
    }

    public static void main(String[] args) {
//        String hashAlgorithmName = "SHA1";
        String hashAlgorithmName = "MD5";
        Object credentials = "123456";
        Object salt = ByteSource.Util.bytes("ticc");
        int hashIterations = 1024;

        Object result = new SimpleHash(hashAlgorithmName, credentials, salt, hashIterations);
        System.out.println(result);
    }

    //授权会被 shiro 回调的方法
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(
            PrincipalCollection principals) {
        //1. 从 PrincipalCollection 中来获取登录用户的信息
//        Object principal = principals.getPrimaryPrincipal();
        SysUserCustom sysUserCustom = (SysUserCustom) principals.getPrimaryPrincipal();
        Map<String,Object> params = Maps.newHashMap();
        params.put("userId",sysUserCustom.getuId());
        params.put("recordStatus", SysConst.RecordStatus.ABLE);
        List<Map<String,Object>> sysRoleCustoms = sysRoleMapper.selectListByUserId(params);
        //2. 利用登录的用户的信息来用户当前用户的角色或权限(可能需要查询数据库)
        Set<String> roles = new HashSet<>();
        Set<Integer> roleIds = Sets.newHashSet();
        Map<String,Object> item;
        if(!CollectionUtils.isEmpty(sysRoleCustoms)) {
            for (int i = 0, len = sysRoleCustoms.size(); i < len; i++){
                item = sysRoleCustoms.get(i);
                roles.add(item.get("roleFlag").toString());
                roleIds.add(Integer.valueOf(item.get("roleId").toString()));
            }
        }

        Set<String> permissions = new HashSet<>();
        if(!CollectionUtils.isEmpty(sysRoleCustoms)) {
            params.clear();
            params.put("roleIds",roleIds);
            params.put("recordStatus", SysConst.RecordStatus.ABLE);
            List<Map<String,Object>> sysPermisCustoms = sysPermisMapper.selectListByRoleId(params);

            Map<String,Object> sysPermisItem;
            for (int i = 0, len = sysPermisCustoms.size(); i < len; i++){
                sysPermisItem = sysPermisCustoms.get(i);
//                permissions.add(sysMenuSysRole.getSysRoleNo()+":"+sysMenuSysRole.getSysMenuNo());
                permissions.add(sysPermisItem.get("pFlag").toString());
            }
        }


//        permissions.add("admin:add");
//        permissions.add("admin:del");

        //3. 创建 SimpleAuthorizationInfo, 并设置其 reles 属性.
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roles);
        info.addStringPermissions(permissions);
        //4. 返回 SimpleAuthorizationInfo 对象.
        return info;
    }

    /**
     * JWTToken刷新生命周期 （实现： 用户在线操作不掉线功能）
     * 1、登录成功后将用户的JWT生成的Token作为k、v存储到cache缓存里面(这时候k、v值一样)，缓存有效期设置为Jwt有效时间的2倍
     * 2、当该用户再次请求时，通过JWTFilter层层校验之后会进入到doGetAuthenticationInfo进行身份验证
     * 3、当该用户这次请求jwt生成的token值已经超时，但该token对应cache中的k还是存在，则表示该用户一直在操作只是JWT的token失效了，程序会给token对应的k映射的v值重新生成JWTToken并覆盖v值，该缓存生命周期重新计算
     * 4、当该用户这次请求jwt在生成的token值已经超时，并在cache中不存在对应的k，则表示该用户账户空闲超时，返回用户信息已失效，请重新登录。
     * 注意： 前端请求Header中设置Authorization保持不变，校验有效性以缓存中的token为准。
     *       用户过期时间 = Jwt有效时间 * 2。
     *
     * @param userName
     * @param passWord
     * @return
     */
    public boolean jwtTokenRefresh(String token, String userName, String passWord) {
        String cacheToken = TokenCache.getKey(SysConst.PREFIX_USER_TOKEN + token);
        if (StringUtils.isNotEmpty(cacheToken)) {
            // 校验token有效性
            if (!JwtUtil.verify(cacheToken, userName, passWord)) {
                String newAuthorization = JwtUtil.sign(userName, passWord);
                // 设置超时时间
                TokenCache.invalidate(SysConst.PREFIX_USER_TOKEN + token);
                TokenCache.setKey(SysConst.PREFIX_USER_TOKEN + token,newAuthorization);
//                redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, newAuthorization);
//                redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME *2 / 1000);
                log.info("——————————用户在线操作，更新token保证不掉线—————————jwtTokenRefresh——————— "+ token);
            }
            //update-begin--Author:scott  Date:20191005  for：解决每次请求，都重写redis中 token缓存问题
//			else {
//				// 设置超时时间
//				redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, cacheToken);
//				redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME / 1000);
//			}
            //update-end--Author:scott  Date:20191005   for：解决每次请求，都重写redis中 token缓存问题
            return true;
        }
        return false;
    }

    /**
     * 清除当前用户的权限认证缓存
     *
     * @param principals 权限信息
     */
    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }
}
