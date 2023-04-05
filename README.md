# PIN PoC

How to deploy:

1. run db & keycloak using `docker-compose up -d`
2. run `mvn package` to build code into .jar
3. copy jar inside `target` directory into `/opt/keycloak/providers` or `./kcpinproviders` directory
4. (optional) in case keycloak show error during registration steps, copy `theme/login` into `/opt/keycloak/themes/pin-auth/login` or `./kcpintheme` directory

Problems and fix:
1. if using default theme always return error or return page without css, change to `keycloak` or `pin-auth` theme
2. need to enable registration option in keycloak
3. don't forget to enable PIN Auth in Authentication > Required Action and enable `set as default action` so after registration user will be asked to enter PIN
4. PIN is stored in `credentials` table but not hashed / encrypted, need to modify `PINAuthRequiredAction` to hash / encrypt before store, and `PINAuthResourceProvider` to hash / encrypt on validate endpoint call