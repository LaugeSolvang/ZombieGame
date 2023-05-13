module Core{
    requires Common;
    requires com.badlogic.gdx;
    requires java.desktop;

    uses common.services.IGamePluginService;
    uses common.services.IEntityProcessingService;
    uses common.services.IPostEntityProcessingService;
    uses common.services.KeyPressListener;


}