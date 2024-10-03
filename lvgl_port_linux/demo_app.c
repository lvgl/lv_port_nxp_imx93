#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <getopt.h>

#include "lvgl/lvgl.h"
#include "lvgl/demos/lv_demos.h"
#include "demo_app.h"

const char *getenv_default(const char *name, const char *dflt)
{
    return getenv(name) ?: dflt;
}

#if LV_USE_LINUX_FBDEV
void lv_linux_disp_init(void)
{
    const char *device = getenv_default("LV_LINUX_FBDEV_DEVICE", "/dev/fb1");
    lv_display_t *disp = lv_linux_fbdev_create();

    lv_linux_fbdev_set_file(disp, device);
}

#elif LV_USE_LINUX_DRM
void lv_linux_disp_init(void)
{
    const char *device = getenv_default("LV_LINUX_DRM_CARD", "/dev/dri/card0");
    lv_display_t *disp = lv_linux_drm_create();

    lv_linux_drm_set_file(disp, device, -1);
}

#elif LV_USE_SDL
void lv_linux_disp_init(void)
{
    lv_sdl_window_create(window_width, window_height);
}

#elif LV_USE_WAYLAND
void lv_linux_disp_init(void)
{
    lv_display_t *disp;
    lv_group_t *g;
    printf("WIDTH: ");
    disp = lv_wayland_window_create(1920, 1080, "LVGL Simulator", NULL);

    if (fullscreen)
    {
        lv_wayland_window_set_fullscreen(disp, fullscreen);
    }
    else if (maximize)
    {
        lv_wayland_window_set_maximized(disp, maximize);
    }

    g = lv_group_create();
    lv_group_set_default(g);
    lv_indev_set_group(lv_wayland_get_keyboard(disp), g);
    lv_indev_set_group(lv_wayland_get_pointeraxis(disp), g);
}
#else
#error Unsupported configuration
#endif

#if LV_USE_WAYLAND
void lv_demo_run_loop(void)
{
    bool completed;

    /* Handle LVGL tasks */
    while (1)
    {

        completed = lv_wayland_timer_handler();

        if (completed)
        {
            /* wait only if the cycle was completed */
            usleep(LV_DEF_REFR_PERIOD * 1000);
        }

        /* Run until the last window closes */
        if (!lv_wayland_window_is_open(NULL))
        {
            break;
        }
    }
}
#else
void lv_demo_run_loop(void)
{
    /*Handle LVGL tasks*/
    while (1)
    {
        lv_timer_handler();
        usleep(5000);
    }
}
#endif